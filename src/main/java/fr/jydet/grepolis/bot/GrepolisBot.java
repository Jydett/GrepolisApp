package fr.jydet.grepolis.bot;

import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.object.util.Snowflake;
import discord4j.rest.service.EmojiService;
import fr.jydet.grepolis.dao.DaoRegistry;
import fr.jydet.grepolis.dao.alliance.AllianceDao;
import fr.jydet.grepolis.dao.island.IslandDao;
import fr.jydet.grepolis.dao.player.PlayerDao;
import fr.jydet.grepolis.dao.stucture.Page;
import fr.jydet.grepolis.dao.stucture.PageRequest;
import fr.jydet.grepolis.dao.stucture.Specification;
import fr.jydet.grepolis.dao.stucture.SpecificationWrapper;
import fr.jydet.grepolis.io.CSVDownloader;
import fr.jydet.grepolis.io.CSVToDatabase;
import fr.jydet.grepolis.model.Alliance;
import fr.jydet.grepolis.model.Displayable;
import fr.jydet.grepolis.model.Island;
import fr.jydet.grepolis.model.Player;
import fr.jydet.grepolis.utils.wagu.Block;
import fr.jydet.grepolis.utils.wagu.Board;
import fr.jydet.grepolis.utils.wagu.Table;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class GrepolisBot {

    private final Logger LOGGER = Logger.getLogger("GrepolisBot");

    private static final Map<String, Command> COMMAND_MAP = new HashMap<>();
    private static final char PREFIX = '!';
    private final CSVToDatabase csvToDatabase;

    public static void main(String[] args) {
        new GrepolisBot().start(args[0]);
    }

    public GrepolisBot() {
        fillMap();
        csvToDatabase = new CSVToDatabase();
    }

    public void start(String token) {
        final DiscordClient client = new DiscordClientBuilder(token).build();
        client.getEventDispatcher().on(MessageCreateEvent.class)
            .filter(event -> ! client.getSelfId().equals(event.getMember().map(User :: getId)))
            .flatMap(event -> Mono.justOrEmpty(event.getMessage().getContent())
                .flatMap(content -> Flux.fromIterable(COMMAND_MAP.entrySet())
                    // We will be using ! as our "prefix" to any command in the system.
                    .filter(entry -> content.startsWith(PREFIX + entry.getKey()))
                    .flatMap(entry -> entry.getValue().execute(event))
                    .next()))
            .subscribe();
        client.login().block();
    }

    private void fillMap() {
        COMMAND_MAP.put("ping",
            e -> e.getMessage()
                .getChannel()
                .flatMap(ch -> ch.createMessage("PONG !").then())
        );
        COMMAND_MAP.put("help",
            e -> e.getMessage()
                .getChannel()
                .flatMap(ch -> ch.createMessage(helpMessage()).then())
        );
        COMMAND_MAP.put("info",
            e -> e.getMessage()
            .getChannel().flatMap(ch -> ch.createMessage(info()).then())
        );
        COMMAND_MAP.put("query",
            e -> e.getMessage()
                .getChannel().flatMap(ch -> ch.createMessage(queryProcessor(e.getMessage())).then())
        );
        COMMAND_MAP.put("update",
            e -> e.getMessage().getChannel()
                .flatMap(ch -> {
                    final String inMessage = e.getMessage().getContent().orElse("");
                    final String[] arguments = inMessage.split(" ");
                    String res;
                    if (arguments.length == 2) {
                        try {
                            final CSVDownloader.DataType dataType = CSVDownloader.DataType.valueOf(arguments[1]);
                            res = CSVDownloader.download(dataType).map(f -> {
                                try {
                                    csvToDatabase.saveFast(dataType, f);
                                } catch (UnsupportedOperationException x) {
                                    return "File downloaded : " + f;
                                }
                                return "File downloaded & uploaded : " + f;
                            }).orElse("Error");
                        } catch (Exception x) {
                            res = ":x: Syntaxe : update <" + Arrays.stream(CSVDownloader.DataType.values()).map(Enum::name).collect(Collectors.joining("/")) + ">";
                        }
                    } else {
                        res = ":x: Syntaxe : update <" + Arrays.stream(CSVDownloader.DataType.values()).map(Enum::name).collect(Collectors.joining("/")) + ">";
                    }

                    return ch.createMessage(res);
                }).then()
        );
    }

    private String queryProcessor(Message message) {
        final String[] args = message.getContent().orElse("").split(" ");
        if (args.length == 3) {
            final CSVDownloader.DataType dataType;
            try {
                dataType = CSVDownloader.DataType.valueOf(args[1]);
            } catch (Exception e) {
                return ":x: Invalid type " + args[1];
            }
            try {
                final Specification<Object> parse = SpecificationWrapper.parse(args[2]);
                switch (dataType) {
                    case PLAYERS:
                        return formatPage(playerDao.findAll(SpecificationWrapper.parse(args[2]), PageRequest.of(0, 10)));
                    default:
                        throw new UnsupportedOperationException("Query on " + dataType + " is not yet supported");
                }
            } catch (Exception e) {
                return ":x: " + e.getMessage();
            }
        }
        return ":x: Syntaxe query <datatype> <rsql query>";
    }

    private String formatPage(Page<?> page) {
        StringBuilder sb = new StringBuilder(String.format("Page %s/%s total : %s\n", page.getNumber(), page.getTotalPages(), page.getTotalElements()));
        if (! page.isEmpty()) {
            final List<?> objects = page.toList();
            List<String> headears = generateHeaders(objects.get(0));
            final Board board = new Board(142);
            List<List<String>> rows = objects.stream().map(this::toRow).collect(Collectors.toList());
            sb.append("```\n");
            List<Integer> colAlignList = Arrays.asList(
                Block.DATA_CENTER,
                Block.DATA_CENTER,
                Block.DATA_CENTER,
                Block.DATA_CENTER,
                Block.DATA_CENTER,
                Block.DATA_CENTER);
            final Table table = new Table(board, 142, headears, rows);
            table.setColAlignsList(colAlignList);
            sb.append(board.setInitialBlock(table.tableToBlocks()).build().getPreview());
            sb.append("```");
        }
        final String res = sb.toString();
        LOGGER.info("SEND : " + res.length());
        return res;
    }

    private List<String> generateHeaders(Object o) {
        if (o instanceof Displayable) {
            return ((Displayable) o).getHeaders();
        }
        return Collections.emptyList();
    }

    private List<String> toRow(Object o) {
        if (o instanceof Displayable) {
            return ((Displayable) o).getRow();
        }
        return Collections.emptyList();

    }


    private final PlayerDao playerDao = DaoRegistry.getDao(Player.class);
    private final AllianceDao allianceDao = DaoRegistry.getDao(Alliance.class);
    private final IslandDao islandDao = DaoRegistry.getDao(Island.class);

    private String info() {
        String sb = "Information sur la base :" +
            "\n Player(s) in base : " + playerDao.count() +
            "\n Alliance(s) in base : " + allianceDao.count() +
            "\n Island(s) in base : " + islandDao.count();
        return sb;
    }

    private String helpMessage() {
        StringBuilder sb = new StringBuilder("Commandes disponibles :\n");
        for (String cmd : COMMAND_MAP.keySet()) {
            sb.append(" - ").append(cmd).append("\n");
        }
        return sb.toString();
    }
}
