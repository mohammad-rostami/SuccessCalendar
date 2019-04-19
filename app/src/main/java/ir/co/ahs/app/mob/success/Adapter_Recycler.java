package ir.co.ahs.app.mob.success;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import ir.co.ahs.app.mob.success.Helper.DataBaseWrite;
import ir.co.persiancalendar.core.G;


//*************************************************************** THIS CLASS IS THE ADAPTER OF RECYCLERVIEWS
public class Adapter_Recycler extends RecyclerView.Adapter<Adapter_Recycler.ViewHolder> {
    public static boolean checked;
    private OnItemListener onItemListener;
    private Context context;
    private ArrayList<Struct> structs;
    private boolean isGrid;
    private int Tab;
    private Struct selectedGroupPosition;
    private Boolean x;
    private Boolean m;
    private RadioButton lastCheckedRadio;
    private LinearLayout LastSelected;
    private LinearLayout LastFontSelected;
    private Typeface typeFace_Light;
    private Typeface typeFace_Medium;
    private Typeface typeFace_Bold;
    ArrayList<String> selected = new ArrayList<>();
    private String selectedId;

    public Adapter_Recycler(Context context, ArrayList<Struct> structs, OnItemListener onItemListener, int Tab, boolean isGrid) {
        this.onItemListener = onItemListener;
        this.context = context;
        this.structs = structs;
        this.isGrid = isGrid;
        this.Tab = Tab;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //CHOOSE WITCH XML (CARD LAYOUT) TO SHOW (INFLATE) FOR EATCH RECYCLERVIEW
        View view = null;

        if (Tab == 1) {
            view = inflater.inflate(R.layout.item_official_event, parent, false);
        }
        if (Tab == 2) {
            view = inflater.inflate(R.layout.item_local_event, parent, false);
        }
        if (Tab == 3) {
            view = inflater.inflate(R.layout.item_fal, parent, false);
        }
        if (Tab == 4) {
            view = inflater.inflate(R.layout.item_news, parent, false);
        }
        if (Tab == 5) {
            view = inflater.inflate(R.layout.item_store, parent, false);
        }
        if (Tab == 6) {
            view = inflater.inflate(R.layout.item_end, parent, false);
        }
        if (Tab == 7) {
            view = inflater.inflate(R.layout.pull_item, parent, false);
        }
        if (Tab == 8) {
            view = inflater.inflate(R.layout.order_item, parent, false);
        }
        if (Tab == 9) {
            view = inflater.inflate(R.layout.credit_item, parent, false);
        }
        if (Tab == 10) {
            view = inflater.inflate(R.layout.message_item, parent, false);
        }
        if (Tab == 12) {
            view = inflater.inflate(R.layout.about_item, parent, false);
        }
        if (Tab == 14) {
            view = inflater.inflate(R.layout.conversation_item, parent, false);
        }
        if (Tab == 15) {
            view = inflater.inflate(R.layout.item_store_menu, parent, false);
        }
        if (Tab == 16) {
            view = inflater.inflate(R.layout.item_target, parent, false);
        }
        if (Tab == 17) {
            view = inflater.inflate(R.layout.item_learn_mini, parent, false);
        }
        if (Tab == 18) {
            view = inflater.inflate(R.layout.item_test, parent, false);
        }
        if (Tab == 19) {
            view = inflater.inflate(R.layout.item_story, parent, false);
        }
        if (Tab == 20) {
            view = inflater.inflate(R.layout.item_learn_mini, parent, false);
        }
        if (Tab == 21) {
            view = inflater.inflate(R.layout.item_story_mini, parent, false);
        }
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        if (Tab == 1) {
            holder.main_item_tv_title.setText(structs.get(position).strItemTitle);
            holder.main_item_tv_date.setText(structs.get(position).strItemDate);
            holder.main_item_tv_date.setVisibility(View.GONE);
        }
        if (Tab == 2) {
            holder.item_title.setText(structs.get(position).strItemTitle);
            holder.item_date.setText(structs.get(position).strItemDate);
            holder.item_time.setText(structs.get(position).strItemTime);
            holder.item_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemListener.onCardClick(position);
                }
            });
        }
        if (Tab == 3) {
            holder.item_title.setText(structs.get(position).strItemTitle);
            holder.item_text.setText(structs.get(position).strItemText);
            holder.item_date.setText(" به روز رسانی در تاریخ " + structs.get(position).strItemDate);
        }
        if (Tab == 4) {

            holder.item_title.setText(structs.get(position).strName);
            holder.item_text.setText(structs.get(position).strText);
            holder.item_date.setText(structs.get(position).strDate);
            Glide.with(G.CONTEXT).load(structs.get(position).strImg).placeholder(R.drawable.news).into(holder.item_image);
        }
        if (Tab == 5) {
            try {
                if (DataBaseChecker("ORDER_TABLE", "ID", structs.get(position).strId)) {
                    holder.item_frame.setBackgroundResource(R.drawable.bg_gradient_disabled);
                } else {
                    holder.item_frame.setBackgroundResource(R.drawable.bg_gradient);
                }
            } catch (Exception e) {
            }


            holder.item_title.setText(structs.get(position).strItemTitle);
            holder.item_text.setText(structs.get(position).strItemText);
            holder.item_price.setText(String.valueOf(structs.get(position).intCurrentPrice));
            holder.item_frame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!DataBaseChecker("ORDER_TABLE", "ID", structs.get(position).strId)) {
                        onItemListener.onItemClick(position);
                        Boolean isRegistered = G.IS_REGISTERED.getBoolean("IS_REGISTERED", false);
                        if (isRegistered) {
                            holder.item_frame.setBackgroundResource(R.drawable.bg_gradient_disabled);
                        }
                    }
                }
            });
            Glide.with(G.CONTEXT).load(structs.get(position).strImg).placeholder(R.drawable.news).into(holder.item_image);

        }
        if (Tab == 6) {
            holder.item_title.setText(structs.get(position).strItemTitle);
            holder.item_text.setText(structs.get(position).strItemText);
            holder.item_count.setText("[ "+String.valueOf(structs.get(position).intQuantity)+" ]");

//            Resources res = getResources();
//            Drawable drawable = res.getDrawable(R.drawable.circular);
            holder.progressBar.setProgress(structs.get(position).intPercent);   // Main Progress
//            mProgress.setSecondaryProgress(100); // Secondary Progress
            holder.progressBar.setMax(100); // Maximum Progress
//            mProgress.setProgressDrawable(drawable);

            holder.item_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemListener.onItemClick(position);
                }
            });
        }
        if (Tab == 7) {

            holder.item_title.setText(structs.get(position).strText);
            holder.item_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemListener.onCardClick(position);
                }
            });

        }
        if (Tab == 8) {
            holder.item_title.setText(structs.get(position).strName);
            String convertedPrice = String.format("%,d", structs.get(position).intCurrentPrice);
            String convertedPrice1 = String.format("%,d", structs.get(position).intPreviousPrice);
            holder.item_price.setText(convertedPrice);
            holder.item_pre_price.setText(convertedPrice1);
            holder.item_text.setText(structs.get(position).strComment);
            if (holder.item_text.getText().toString().length() < 2) {
                holder.item_text.setText("بدون توضیحات");
            }
            holder.item_number.setText(String.valueOf(structs.get(position).intQuantity));
            holder.item_number.setFocusable(false);
            holder.item_number.setFocusableInTouchMode(false);
            holder.item_number.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemListener.onCardClick(position);
                }
            });

            Glide.with(G.CONTEXT).load(structs.get(position).strImg).into(holder.item_image_round);
            if (structs.get(position).intQuantity == 1) {
                holder.item_minus.setColorFilter(G.CONTEXT.getResources().getColor(R.color.brightGray));
            } else {
                holder.item_minus.setColorFilter(G.CONTEXT.getResources().getColor(R.color.black));
            }

            holder.item_plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String number = holder.item_number.getText().toString();
                    int no = Integer.parseInt(number);
                    holder.item_number.setText(String.valueOf(no + 1));
                    onItemListener.onItemSelect(position);
                    if (structs.get(position).intQuantity > 1) {
                        holder.item_minus.setColorFilter(G.CONTEXT.getResources().getColor(R.color.black));
                    }
                }
            });
            holder.item_minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (structs.get(position).intQuantity > 1) {
                        String number = holder.item_number.getText().toString();
                        int no = Integer.parseInt(number);
                        holder.item_number.setText(String.valueOf(no - 1));
                        onItemListener.onItemClick(position);
                        if (structs.get(position).intQuantity == 1) {
                            holder.item_minus.setColorFilter(G.CONTEXT.getResources().getColor(R.color.brightGray));
                        }
                    }
                }
            });
            holder.item_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemListener.onItemDelete(position);
                }
            });
            holder.item_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemListener.onCardClick(position);
                }
            });
        }
        if (Tab == 9) {

            holder.item_title.setText(structs.get(position).strItemTitle);
            String value = String.format("%,d", Integer.parseInt(structs.get(position).strText));
            holder.item_price.setText(value);
            if (structs.get(position).isAdded) {
                holder.item_price.setTextColor(G.CONTEXT.getResources().getColor(R.color.Green));
            } else {
                holder.item_price.setTextColor(G.CONTEXT.getResources().getColor(R.color.Red));
            }
            holder.item_date.setText(structs.get(position).strDate);

        }
        if (Tab == 10) {

            holder.item_text.setText(structs.get(position).strText);
            holder.item_date.setText(structs.get(position).strDate);
            holder.item_time.setText(structs.get(position).strTime);
            holder.item_title.setText(structs.get(position).strName);
            final float scale = G.CONTEXT.getResources().getDisplayMetrics().density;
            if (structs.get(position).sender) {
                holder.item_text.setBackgroundResource(R.drawable.bg_message_frame1);
                holder.item_text.setGravity(Gravity.RIGHT);
                holder.item_text.setTextColor(G.CONTEXT.getResources().getColor(R.color.white));

                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.item_text.getLayoutParams();
                int pixel_left = (int) (40 * scale + 0.5f);
                int pixel_top = (int) (4 * scale + 0.5f);
                int pixel_right = (int) (10 * scale + 0.5f);
                int pixel_bottom = (int) (4 * scale + 0.5f);
                params.setMargins(pixel_left, pixel_top, pixel_right, pixel_bottom); //substitute parameters for left, top, right, bottom
                holder.item_text.setLayoutParams(params);
            } else {
                holder.item_text.setBackgroundResource(R.drawable.bg_message_frame2);
                holder.item_text.setTextColor(G.CONTEXT.getResources().getColor(R.color.black));
                holder.item_text.setGravity(Gravity.RIGHT);

                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.item_text.getLayoutParams();
                int pixel_left = (int) (10 * scale + 0.5f);
                int pixel_top = (int) (4 * scale + 0.5f);
                int pixel_right = (int) (40 * scale + 0.5f);
                int pixel_bottom = (int) (4 * scale + 0.5f);
                params.setMargins(pixel_left, pixel_top, pixel_right, pixel_bottom); //substitute parameters for left, top, right, bottom
                holder.item_text.setLayoutParams(params);

            }


        }
        if (Tab == 12) {

            holder.item_title.setText(structs.get(position).strItemTitle);
            if (structs.get(position).strType.equals("web")) {

                holder.item_text.setTextColor(G.CONTEXT.getResources().getColor(R.color.Blue));
                SpannableString content = new SpannableString(structs.get(position).strText);
                content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                holder.item_text.setText(content);

                holder.item_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onItemListener.onItemClick(position);
                    }
                });

            } else if (structs.get(position).strType.equals("call")) {
                holder.item_text.setText(structs.get(position).strText);
                holder.item_text.setTextColor(G.CONTEXT.getResources().getColor(R.color.Green));
                holder.item_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onItemListener.onItemSelect(position);
                    }
                });
            } else if (structs.get(position).strType.equals("email")) {
                holder.item_text.setText(structs.get(position).strText);
                holder.item_text.setTextColor(G.CONTEXT.getResources().getColor(R.color.Red));
                holder.item_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onItemListener.onCardClick(position);
                    }
                });
            } else {
                holder.item_text.setText(structs.get(position).strText);
                holder.item_text.setTextColor(G.CONTEXT.getResources().getColor(R.color.black));

            }


        }
        if (Tab == 14) {


            holder.item_title.setText(structs.get(position).strItemTitle);
            holder.item_text.setText(structs.get(position).strText);
            holder.item_date.setText(structs.get(position).strDate);
//            holder.time.setText(structs.get(position).strTime);
            holder.item_sender.setText(structs.get(position).strName);
            holder.item_frame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemListener.onItemClick(position);
                }
            });
            if (structs.get(position).intQuantity == 0) {
                holder.item_unread.setVisibility(View.GONE);
            } else {
                holder.item_unread.setVisibility(View.VISIBLE);
                holder.item_unread.setText(String.valueOf(structs.get(position).intQuantity));

            }
        }
        if (Tab == 15) {


            holder.item_title.setText(structs.get(position).strItemTitle);
            holder.item_frame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemListener.onItemClick(position);
                }
            });


        }
        if (Tab == 16) {


            holder.item_title.setText(structs.get(position).strItemTitle);
            holder.item_check.setChecked(structs.get(position).isDone);
            holder.item_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    onItemListener.onItemSelect(position);

                }
            });
            holder.item_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //creating a popup menu
                    PopupMenu popup = new PopupMenu(G.CONTEXT, holder.item_menu);
                    //inflating menu from xml resource
                    popup.inflate(R.menu.options_menu);
                    //adding click listener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.menu1:
                                    //handle menu1 click
                                    onItemListener.onItemDelete(position);
                                    break;
                                case R.id.menu2:
                                    //handle menu2 click
                                    onItemListener.onCardClick(position);
                                    break;
                            }
                            return false;
                        }
                    });
                    //displaying the popup
                    popup.show();
                }
            });

        }
        if (Tab == 17) {

            holder.item_title.setText(structs.get(position).strName);
            holder.item_text.setText(structs.get(position).strText);
            holder.item_date.setText(structs.get(position).strDate);
            Glide.with(G.CONTEXT).load(structs.get(position).strImg).placeholder(R.drawable.news).into(holder.item_image);

            holder.item_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemListener.onCardClick(position);
                }
            });
        }
        if (Tab == 18) {
            View.OnClickListener onclicklistener = null;
            onclicklistener = new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    JSONArray items = null;
                    JSONObject data = structs.get(position).jsonObject;

                    try {
                        JSONArray itemsArray = data.getJSONArray("answerList");
                        JSONObject item = itemsArray.getJSONObject(v.getId());
//                        itemID = item.getString("answerID");
                    } catch (JSONException e) {
                    }
                    selected.add(String.valueOf(position) + ":" + String.valueOf(v.getId()));

                }
            };

            selected = null;
            for (int i = 0; i < selected.size(); i++) {
                String CurrentString = selected.get(i);
                String[] separated = CurrentString.split(":");
                String positionNo = separated[0];
                String id = separated[1];
                if (positionNo.equals(String.valueOf(position))) {
                    selectedId = id;
                }
            }
            holder.item_title.setText(structs.get(position).strItemTitle);
            JSONObject data = structs.get(position).jsonObject;
            JSONArray answers = null;

            try {
                answers = data.getJSONArray("answerList");

                holder.item_frame.removeAllViews();

                RadioGroup radioGroup = new RadioGroup(G.CONTEXT);
                radioGroup.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                holder.item_frame.addView(radioGroup);

                for (int i = 0; i < answers.length(); i++) {
                    JSONObject item = answers.getJSONObject(i);
                    RadioButton radioButton = new RadioButton(G.CONTEXT);
                    radioButton.setId(i);
                    radioButton.setOnClickListener(onclicklistener);
                    radioButton.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    radioGroup.addView(radioButton);
                    radioButton.setText(item.getString("answerTitle"));
                    radioButton.setPadding(0, 10, 0, 10);
//                            struct.strItemId = item.getString("ItemID");
//                            struct.isDeletedItem = item.getBoolean("isDeletedItem");
                    radioButton.setTypeface(typeFace_Light);
                    radioButton.setChecked(true);

//                    try {
//                        if (i == Integer.parseInt(selectedId))
//                            radioButton.setSelected(true);
//                    } catch (Exception e) {
//                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

//            holder.item_date.setText(structs.get(position).strDate);
//            Glide.with(G.CONTEXT).load(structs.get(position).strImg).placeholder(R.drawable.news).into(holder.item_image);
//
//            holder.item_card.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    onItemListener.onCardClick(position);
//                }
//            });


        }
        if (Tab == 19) {

            holder.item_title.setText(structs.get(position).strName);
            holder.item_title1.setText(structs.get(position).strItemTitle);
            holder.item_text.setText(structs.get(position).strText);
            holder.item_date.setText(structs.get(position).strDate);
            Glide.with(G.CONTEXT).load(structs.get(position).strImg).placeholder(R.drawable.news).into(holder.item_image);

//            holder.item_card.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    onItemListener.onCardClick(position);
//                }
//            });
        }
        if (Tab == 20) {

            holder.item_title.setText(structs.get(position).strName);
            holder.item_text.setText(structs.get(position).strText);
            holder.item_date.setText(structs.get(position).strDate);
            Glide.with(G.CONTEXT).load(structs.get(position).strImg).placeholder(R.drawable.news).into(holder.item_image);

            holder.item_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemListener.onCardClick(position);
                }
            });
        }
        if (Tab == 21) {

            holder.item_title.setText(structs.get(position).strName);
            holder.item_title1.setText(structs.get(position).strItemTitle);
//            holder.item_text.setText(structs.get(position).strText);
            holder.item_date.setText(structs.get(position).strDate);
//            Glide.with(G.CONTEXT).load(structs.get(position).strImg).placeholder(R.drawable.news).into(holder.item_image);

            holder.item_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemListener.onCardClick(position);
                }
            });
        }


    }


    @Override
    public int getItemCount() {
        return structs.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout item_ll;
        private final TextView item_sender;
        private final TextView item_unread;
        private final LinearLayout item_frame;
        private final ImageView item_plus;
        private final ImageView item_minus;
        private final TextView item_number;
        ImageView item_delete;
        ImageView item_image;
        ImageView item_menu;
        ImageView item_image_round;
        TextView item_count;
        TextView main_item_tv_title;
        TextView main_item_tv_date;
        TextView item_title;
        TextView item_title1;
        TextView item_text;
        TextView item_date;
        TextView item_time;
        TextView item_price;
        TextView item_pre_price;
        ProgressBar progressBar;
        CardView item_card;
        RelativeLayout item_layout;
        CheckBox item_check;

        public ViewHolder(final View itemView) {
            super(itemView);
            item_ll = (LinearLayout) itemView.findViewById(R.id.item_ll);
            item_frame = (LinearLayout) itemView.findViewById(R.id.item_frame);
            item_menu = (ImageView) itemView.findViewById(R.id.item_menu);
            main_item_tv_title = (TextView) itemView.findViewById(R.id.main_item_tv_title);
            main_item_tv_date = (TextView) itemView.findViewById(R.id.main_item_tv_date);
            item_count = (TextView) itemView.findViewById(R.id.item_count);
            item_title = (TextView) itemView.findViewById(R.id.item_title);
            item_title1 = (TextView) itemView.findViewById(R.id.item_title1);
            item_text = (TextView) itemView.findViewById(R.id.item_text);
            item_date = (TextView) itemView.findViewById(R.id.item_date);
            item_time = (TextView) itemView.findViewById(R.id.item_time);
            item_price = (TextView) itemView.findViewById(R.id.item_price);
            item_pre_price = (TextView) itemView.findViewById(R.id.product_previous_price);
            item_plus = (ImageView) itemView.findViewById(R.id.item_plus);
            item_minus = (ImageView) itemView.findViewById(R.id.item_minus);
            item_number = (TextView) itemView.findViewById(R.id.item_number);
            item_sender = (TextView) itemView.findViewById(R.id.item_sender);
            item_unread = (TextView) itemView.findViewById(R.id.item_unread);
            item_image = (ImageView) itemView.findViewById(R.id.item_image);
            item_image_round = (ImageView) itemView.findViewById(R.id.item_image_round);
            item_delete = (ImageView) itemView.findViewById(R.id.item_delete);
            progressBar = (ProgressBar) itemView.findViewById(R.id.timer_progress);
            item_card = (CardView) itemView.findViewById(R.id.item_card);
            item_layout = (RelativeLayout) itemView.findViewById(R.id.item_layout);
            item_check = (CheckBox) itemView.findViewById(R.id.item_check);


        }

    }

    private boolean DataBaseChecker(String TableName, String ID, String SELECTED_ID) {
        Boolean isAvailable = false;
        DataBaseWrite dataBaseHelper = new DataBaseWrite(G.CONTEXT, "ORDER_DATABASE", "ORDER_TABLE", G.OrderTableFieldNames, G.OrderTableFieldTypes);
        try {
            dataBaseHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        SQLiteDatabase sqld = dataBaseHelper.getWritableDatabase();

        Cursor cursor = sqld.rawQuery("select * from " + TableName + " where ID = \"" + SELECTED_ID + "\"", null);
        String id;
        if (cursor.moveToFirst()) {
            do {
                Log.d("table", "item found");
                isAvailable = true;
            } while (cursor.moveToNext());
        }
        sqld.close();
        return isAvailable;
    }

}
