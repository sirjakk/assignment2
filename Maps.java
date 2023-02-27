public class Maps {
    private String name;
    private String secret;
    private String fileName;
    private int year;
    private boolean isEncrypted = true;

    public Maps(String name, int year, String fileName) {
        this.fileName = fileName;
        this.name = name;
        this.year = year;
    }

    public String getName() {
        return name;
    }

    public int getYear() {
        return year;
    }

    public boolean isEncrypted() {
        return isEncrypted;
    }

    public String getSecret() {
        return secret;
    }

    public String getFileName() {
        return fileName;
    }

    public void updateSecret(String secret) {
        this.secret = secret;
        isEncrypted = false;
    }

    public String toString() {
        return "Name: " + name + ", Year: " + year + ", Is It Encrypted: " + isEncrypted + ", Secret: " + secret;
    }
}