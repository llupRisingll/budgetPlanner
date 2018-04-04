package capstone.androiduserbadgetplanner;

public class Expenses {

    private int _id;
    private int _amount;


    private int _saving;
    private String _category;


    // Constructors
    public Expenses(int amount, String category, int saving) {
        _amount = amount;
        _category = category;
        _saving = saving;
    }

    // Setters
    public void set_id(int _id) {
        this._id = _id;
    }

    public void set_saving(int _saving) {
        this._saving = _saving;
    }
    public void set_amount(int _amount) {
        this._amount = _amount;
    }


    // Getters
    public int get_id() {
        return _id;
    }

    public int get_amount() {
        return _amount;
    }
    public int get_saving() {
        return _saving;
    }

    public String get_category() {
        return _category;
    }
}
