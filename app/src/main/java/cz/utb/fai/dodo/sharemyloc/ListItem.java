package cz.utb.fai.dodo.sharemyloc;

/**
 * Created by Dodo on 29.01.2018.
 */

public class ListItem {

    // // TODO: 29.01.2018 add image
    private String fullName;
    private boolean checked;

    public ListItem(String fullName, boolean checked) {
        this.fullName = fullName;
        this.checked = checked;
    }

    public String getFullName() {
        return fullName;
    }

    public boolean isChecked() {
        return checked;
    }
}
