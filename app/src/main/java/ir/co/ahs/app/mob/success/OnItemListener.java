package ir.co.ahs.app.mob.success;


//************************ CLICK LISTENER FOR RECYCLERVIEW ITEMS

public interface OnItemListener {
    public void onItemSelect(int position);

    public void onItemClick(int position);

    public void onItemDelete(int position);

    public void onCardClick(int position);

    public void onEdit(int position);
}
