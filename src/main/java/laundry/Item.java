package laundry;

// Dipakai untuk isi JComboBox (menyimpan id, teks tampilan, dan harga)
public class Item {

    public final int id;
    public final String label;
    public final double harga;

    public Item(int id, String label, double harga) {
        this.id = id;
        this.label = label;
        this.harga = harga;
    }

    @Override
    public String toString() {
        return label;
    }
}
