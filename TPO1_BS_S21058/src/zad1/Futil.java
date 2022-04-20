package zad1;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class Futil {
    static FileChannel outChanel;
    static Charset inCharset = Charset.forName("Cp1250");
    static Charset outCharset = Charset.forName("UTF-8");
    static CharBuffer charBuffer;
    static FileChannel inChanel;

    public static void processDir(String dirName, String resultFileName) {

        try {
            outChanel = FileChannel.open(Paths.get(resultFileName), StandardOpenOption.CREATE, StandardOpenOption.WRITE);

        } catch (IOException exception) {
            exception.printStackTrace();
        }
        try {
            Files.walkFileTree(Paths.get(dirName), new SimpleFileVisitor() {
                @Override
                public FileVisitResult visitFile(Object file, BasicFileAttributes attrs) throws IOException {
                    inChanel = FileChannel.open((Path) file);
                    ByteBuffer buffer = ByteBuffer.allocate(256);
                    inChanel.read(buffer);
                    buffer.flip();
                    /**
                     * FROM Cp1250 TO UTF-8
                     **/
                    charBuffer = inCharset.decode(buffer);
                    buffer = outCharset.encode(charBuffer);
                    outChanel.write(buffer);
                    outChanel.write(ByteBuffer.wrap(System.getProperty("line.separator").getBytes()));
                    inChanel.close();
                    buffer.clear();

                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
