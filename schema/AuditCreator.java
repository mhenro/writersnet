import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//branchpaymentsmethods - branchid, methodid - both PK
//branchpointofsales - branchid, posid - both PK
//modified_at timestamp without time zone, - without PK should remove comma

/*
    This class generates audit sql tables for the given sql tables
 */
public class AuditCreator {
    private static final Pattern namePattern = Pattern.compile("^CREATE TABLE public.([A-Za-z0-9]+) \\(");
    private static final Pattern pkPattern = Pattern.compile("^PRIMARY KEY \\(([A-Za-z0-9,_ ]+)\\)");

    public static void main(String args[]) {
        if (args.length < 1) {
            System.out.println("Usage: AuditCreator file.sql");
            return;
        }
        System.out.println("Creating audit tables for sql file " + args[0]);

        try {
            File readFile = new File(args[0]);
            File writeFile = new File("audit.sql");
            FileOutputStream fos = new FileOutputStream(writeFile);
            BufferedReader reader = new BufferedReader(new FileReader(readFile));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));

            writeAuditHeader(writer);
            processSqlFile(reader, writer);
            writer.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeAuditHeader(BufferedWriter writer) {
        try {
            writer.write("-- noinspection SqlNoDataSourceInspectionForFile");
            writer.newLine();
            writer.newLine();
            writer.write("CREATE TABLE custom_audit_entity (");
            writer.newLine();
            writer.write("  id INTEGER NOT NULL,");
            writer.newLine();
            writer.write("  timestamp BIGINT NOT NULL,");
            writer.newLine();
            writer.write("  user_id UUID,");
            writer.newLine();
            writer.write("  PRIMARY KEY (id)");
            writer.newLine();
            writer.write(");");
            writer.newLine();
            writer.newLine();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private static void processSqlFile(BufferedReader reader, BufferedWriter writer) {
        String line = "";
        String tableName = "";
        Boolean tableStarted = false;
        Boolean hasId = false;

        try {
            while ((line = reader.readLine()) != null) {
                if (line.trim().startsWith("CREATE TABLE")) {   //CREATE TABLE public.products (
                    tableStarted = true;
                    String newLine = line.substring(0, line.length() - 2);
                    writer.write(newLine + "_aud (");
                    writer.newLine();
                    writer.write("  revision_id INTEGER NOT NULL,");
                    writer.newLine();
                    writer.write("  revision_type INT2,");
                    writer.newLine();

                    Matcher matcher = namePattern.matcher(line.trim());
                    if (matcher.find()) {
                        tableName = matcher.group(1);
                    }
                } else if (!tableStarted) {
                    continue;
                } else if (line.trim().startsWith("id ")) {
                    hasId = true;
                    writer.write(line);
                    writer.newLine();
                } else if (line.trim().startsWith("PRIMARY KEY")) {
                    Matcher matcher = pkPattern.matcher(line.trim());
                    if (matcher.find()) {
                        if (!isContain(line, "id")) {
                            writer.write(",");
                            writer.newLine();
                        }
                        writer.write("  PRIMARY KEY (" + matcher.group(1) + ", revision_id)");
                        writer.newLine();
                    }
                } else if (line.trim().startsWith("modified_at timestamp without time zone,")) {
                    if (hasId) {
                        writer.write(line);
                        writer.newLine();
                    } else {
                        writer.write("modified_at timestamp without time zone");
                        writer.newLine();
                    }
                } else if (line.trim().startsWith(");")) {
                    tableStarted = false;
                    hasId = false;

                    writer.write(");");
                    writer.newLine();
                    writer.newLine();
                    writer.write("ALTER TABLE " + tableName + "_aud");
                    writer.newLine();
                    writer.write("  ADD CONSTRAINT FK_" + tableName + "_aud_audit");
                    writer.newLine();
                    writer.write("  FOREIGN KEY (revision_id)");
                    writer.newLine();
                    writer.write("  REFERENCES custom_audit_entity;");
                    writer.newLine();
                    writer.newLine();
                } else if (line.trim().startsWith("opt_lock bigint NOT NULL")) {
                    continue;
                } else {
                    writer.write(line);
                    writer.newLine();
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean isContain(String source, String subItem){
        String pattern = "\\b" + subItem + "\\b";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(source);
        return m.find();
    }
}