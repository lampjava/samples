package com.replace.sample;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileReplacer {

    private static final BiPredicate<Path, BasicFileAttributes> predicateRegularFile = (p, bfa) -> bfa.isRegularFile()
            && p.toFile().exists();

    private static final BiPredicate<Path, BasicFileAttributes> findByFileNamePattern(final Pattern pattern) {
        return (p, bfa) -> pattern.matcher(p.toFile().toURI().toString()).find();
    }

    private static final String replaceWord(final String v, final Map<String, String> wordsToReplace) {
        Set<String> keys = wordsToReplace.keySet();
        String a = v;
        for (String key : keys) {
            a = a.replace(key, wordsToReplace.get(key));
        }
        return a;
    }

    final Map<String, String> wordsToReplace = wordsToReplace();

    Map<String, String> wordsToReplace() {
        final Map<String, String> wordsToReplace = new HashMap<String, String>();
        // wordsToReplace.put("are", "r");
        // wordsToReplace.put("you", "u");
        // wordsToReplace.put("to", "2");

        wordsToReplace.put("\":false", "\":0");
        wordsToReplace.put("\":true", "\":1");
        wordsToReplace.put("\"aid_GstxIWObRA_p_salescode\":\"HUI\"", "\"aid_gstxiwobra_p_salescode\":\"HuI\"");
        //
        return wordsToReplace;
    }

    private Consumer<File> fileConsumer = new Consumer<File>() {

        @Override
        public void accept(final File file) {
            System.out.println(file.getAbsolutePath());

            try {
                Path path = Paths.get(file.toURI());
                Stream<String> lines = Files.lines(path);
                List<String> replaced = lines.map(v -> replaceWord(v, wordsToReplace)).collect(Collectors.toList());
                Files.write(path, replaced);
                lines.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    private List<File> files(final File file) {
        final Path start = Paths.get(file.toURI());
        final Pattern pattern = Pattern.compile("[\\*.prd]$");
        try {
            return Files.find(start, Integer.MAX_VALUE, predicateRegularFile.and(findByFileNamePattern(pattern))).map(v -> v.toFile())
                    .sorted() /* */
                    .collect(Collectors.toList());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void doExecute(final File path) {
        List<File> files = files(path);
        files.stream().forEach(fileConsumer);
    }

    public static void main(String[] args) {

        final FileReplacer replacer = new FileReplacer();
        final File path = new File("/log/torepl");
        replacer.doExecute(path);
    }

}
