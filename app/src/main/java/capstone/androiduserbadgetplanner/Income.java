package capstone.androiduserbadgetplanner;

public class Income {

    private int _id;
    private int _amount;


    // Constructors
    public Income(int amount) {
        _amount = amount;
    }

    // Setters
    public void set_id(int _id) {
        this._id = _id;
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

}
