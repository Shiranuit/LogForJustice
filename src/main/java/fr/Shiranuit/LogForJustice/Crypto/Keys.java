package fr.Shiranuit.LogForJustice.Crypto;

public class Keys {

    public String priv;
    public String pub;
    public String n;

    public Keys(String pub, String priv, String n) {
        this.priv = priv;
        this.pub = pub;
        this.n = n;
    }

    public String toString() {
        return "["+pub+", "+priv+", "+n+"]";
    }
}
