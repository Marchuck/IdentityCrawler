package pl.marczak.identitycrawler;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemViewHolder;

import java.util.ArrayList;
import java.util.List;

import pl.lukaszmarczak.expandabledelegates.expandable_delegates.DelegableChildAdapter;
import pl.lukaszmarczak.expandabledelegates.expandable_delegates.DelegableParentAdapter;
import pl.lukaszmarczak.expandabledelegates.expandable_delegates.DelegatesAdapter;
import pl.lukaszmarczak.expandabledelegates.expandable_delegates.DelegatesManager;
import pl.lukaszmarczak.expandabledelegates.expandable_delegates.XChild;
import pl.lukaszmarczak.expandabledelegates.expandable_delegates.XParent;
import rx.Observable;

/**
 * @author Lukasz Marczak
 * @since 04.06.16.
 */
public class ExpandableWrapper {
    public static final String TAG = ExpandableWrapper.class.getSimpleName();
    public static final String ANONYMOUS = "Anonymous";
    public static final int BLANK = 7;
    public static final int PARENT_VIEWTYPE_1 = 1;
    public static final int PARENT_VIEWTYPE_2 = 2;

    public static final int CHILD_VIEWTYPE = 3;
    public static final int CHILD_VIEWTYPE_2 = 4;

    public static Observable<RecyclerView.Adapter> getAdapter() {
        return Observable.defer(() -> Observable.just(createAdapter()));
    }

    public static Observable<RecyclerView.Adapter> getGoogleMapAdapter(View v) {
        return Observable.defer(() -> Observable.just(createGoogleAdapter(v)));
    }

    private static RecyclerView.Adapter createGoogleAdapter(View stealTouchEventsView) {
        DelegatesManager<Parent, Child> manager = new DelegatesManager<>();
        manager.addDelegateChild(new ExtendedChildAdapter());
        manager.addDelegateParent(new ParentAdapter());
        manager.addDelegateParent(new HeaderParentAdapter());
        manager.addDelegateParent(new BlankParentAdapter(stealTouchEventsView));

        List<XParent<Parent, Child>> dataSet = new ArrayList<>();

        int wawaColor = Color.parseColor("#ff0055");
        int gdColor = Color.parseColor("#0ea169");
        int wrocColor = Color.parseColor("#c4750f");

        List<XChild<Child>> wawaChildren = new ArrayList<>();
        wawaChildren.add(new XChild<>(new Child("beer", R.drawable.beer, wawaColor), CHILD_VIEWTYPE_2));
        wawaChildren.add(new XChild<>(new Child("cinema", R.drawable.play, wawaColor), CHILD_VIEWTYPE_2));
        wawaChildren.add(new XChild<>(new Child("college", R.drawable.school, wawaColor), CHILD_VIEWTYPE_2));
        wawaChildren.add(new XChild<>(new Child("taxi", R.drawable.car, wawaColor), CHILD_VIEWTYPE_2));

        List<XChild<Child>> wroc = new ArrayList<>();
        wroc.add(new XChild<>(new Child("college", R.drawable.school, wrocColor), CHILD_VIEWTYPE_2));
        wroc.add(new XChild<>(new Child("taxi", R.drawable.car, wrocColor), CHILD_VIEWTYPE_2));

        List<XChild<Child>> gdy = new ArrayList<>();
        gdy.add(new XChild<>(new Child("beer", R.drawable.beer, gdColor), CHILD_VIEWTYPE_2));

        dataSet.add(new XParent<>(new Parent(), new ArrayList<>(), BLANK));
        dataSet.add(new XParent<>(new Parent("Popular cities", R.drawable.wawa, Color.BLACK), new ArrayList<>(), PARENT_VIEWTYPE_2));
        dataSet.add(new XParent<>(new Parent("Warszawa", R.drawable.wawa, wawaColor), wawaChildren, PARENT_VIEWTYPE_1));
         dataSet.add(new XParent<>(new Parent("Gdańsk", R.drawable.gda, gdColor), gdy, PARENT_VIEWTYPE_1));
        dataSet.add(new XParent<>(new Parent("Wrocław", R.drawable.wroc, wrocColor), wroc, PARENT_VIEWTYPE_1));

        DelegatesAdapter<Parent, Child> delegatesAdapter = new DelegatesAdapter<>(dataSet, manager);
        return delegatesAdapter;
    }

    private static RecyclerView.Adapter createAdapter() {
        DelegatesManager<Parent, Child> manager = new DelegatesManager<>();
        manager.addDelegateChild(new ExtendedChildAdapter());
        manager.addDelegateParent(new ParentAdapter());
        manager.addDelegateParent(new HeaderParentAdapter());

        List<XParent<Parent, Child>> dataSet = new ArrayList<>();

        List<XChild<Child>> children = new ArrayList<>();
        children.add(new XChild<>(new Child("sleep", R.drawable.cool), CHILD_VIEWTYPE));
        children.add(new XChild<>(new Child("eat", R.drawable.pops), CHILD_VIEWTYPE));
        children.add(new XChild<>(new Child("dance", R.drawable.smallimage), CHILD_VIEWTYPE_2));
        children.add(new XChild<>(new Child("repeat", android.R.drawable.btn_star_big_on), CHILD_VIEWTYPE));

        List<XChild<Child>> children2 = new ArrayList<>();
        children2.add(new XChild<>(new Child("sleep", android.R.drawable.ic_delete), CHILD_VIEWTYPE_2));
        children2.add(new XChild<>(new Child("eat", android.R.drawable.ic_dialog_info), CHILD_VIEWTYPE_2));
        children2.add(new XChild<>(new Child("play", android.R.drawable.ic_input_add), CHILD_VIEWTYPE));

        List<XChild<Child>> children3 = new ArrayList<>();
        children3.add(new XChild<>(new Child("what", R.drawable.pops), CHILD_VIEWTYPE));
        children3.add(new XChild<>(new Child("the", R.drawable.pops), CHILD_VIEWTYPE_2));
        children3.add(new XChild<>(new Child("???", R.drawable.cool), CHILD_VIEWTYPE));
        children3.add(new XChild<>(new Child("???", R.drawable.cool), CHILD_VIEWTYPE_2));


        dataSet.add(new XParent<>(new Parent("Friday", R.drawable.waves, 0), children2, PARENT_VIEWTYPE_1));
        dataSet.add(new XParent<>(new Parent("Monday", R.drawable.cool, 0), children2, PARENT_VIEWTYPE_2));


        DelegatesAdapter<Parent, Child> delegatesAdapter = new DelegatesAdapter<>(dataSet, manager);
        return delegatesAdapter;
    }

    public static class Parent {
        public boolean clicked;
        public int res;
        public int color;

        public Parent() {
        }

        public Parent(String parentName, int res, int color) {
            this.parentName = parentName;
            this.res = res;
            this.color = color;
        }

        public String parentName;


    }

    public static class Child {
        public String childName;
        public int backgroundColor;
        public int res;

        public Child(String childName, int res, int backgroundColor) {
            this.childName = childName;
            this.res = res;
            this.backgroundColor = backgroundColor;
        }

        public Child(String childName, int res) {
            this.childName = childName;
            this.res = res;
        }

        @Override
        public String toString() {
            return "childName: " + childName +
                    ", backgroundColor: " + backgroundColor +
                    ", res: " + res;
        }
    }

    public static class ExtendedChildAdapter extends DelegableChildAdapter<Parent, Child> {

        @Override
        public int getChildViewType() {
            return CHILD_VIEWTYPE_2;
        }

        @Override
        public void onBindChildViewHolder(RecyclerView.ViewHolder holder, int groupPosition,
                                          int childPosition, int viewType) {
            XChild<Child> childWrapper = getDataSet().get(groupPosition).getChildren().get(childPosition);
            Child child = childWrapper.getChild();
            ChildViewHolder vh = (ChildViewHolder) holder;
            vh.textView.setTextColor(child.backgroundColor);
            vh.divider.setBackgroundColor(child.backgroundColor);
            vh.textView.setText(child.childName);
            vh.rootView.setBackgroundColor(Color.parseColor("#c9c1ac"));
            vh.imageView.setImageResource(child.res);
            Log.d(TAG, "child: " + child.toString());
        }

        @Override
        public RecyclerView.ViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
            View v = DelegatesManager.inflateMe(parent, R.layout.child_item_2);
            return new ChildViewHolder(v);
        }
    }

    public static class ChildViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        View rootView;
        ImageView imageView;
        View divider;

        public ChildViewHolder(View itemView) {
            super(itemView);
            rootView = itemView.findViewById(R.id.item_root_1);
            textView = (TextView) rootView.findViewById(R.id.text_child);
            imageView = (ImageView) rootView.findViewById(R.id.image_child);
            divider = rootView.findViewById(R.id.divider);
        }
    }


    public static class BlankParentAdapter extends DelegableParentAdapter<Parent, Child> {
        public BlankParentAdapter(View stealTouchEventsView) {
            this.stealTouchEventsView = stealTouchEventsView;
            Log.d(TAG, "BlankParentAdapter: ");
        }

        private View stealTouchEventsView;

        @Override
        public int getParentViewType() {
            return BLANK;
        }

        @Override
        public void onBindGroupViewHolder(AbstractExpandableItemViewHolder holder, final int groupPosition, int viewType) {
        }

        @Override
        public AbstractExpandableItemViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
            View v = DelegatesManager.inflateMe(parent, R.layout.blank_parent);
            return new AVH(v, stealTouchEventsView);
        }

        public static class AVH extends AbstractExpandableItemViewHolder {

            public AVH(View itemView, View stealTouchEventsView) {
                super(itemView);
//                itemView.setBackgroundColor(Color.TRANSPARENT);
                itemView.setOnTouchListener((v, event) -> {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    return stealTouchEventsView.dispatchTouchEvent(event);
                });
            }
        }
    }

    public static class ParentAdapter extends DelegableParentAdapter<Parent, Child> {

        @Override
        public int getParentViewType() {
            return PARENT_VIEWTYPE_1;
        }

        @Override
        public void onBindGroupViewHolder(AbstractExpandableItemViewHolder holder, final int groupPosition, int viewType) {
            final AVH avh = (AVH) holder;
            final Parent parentData = getDataSet().get(groupPosition).getParent();
            avh.parentName.setText(parentData.parentName);
            avh.imageView.setImageResource(parentData.res);
            avh.root.setBackgroundColor(parentData.color);
            avh.itemView.setOnClickListener(v -> {
                Log.d(TAG, "onClick: ");
                parentData.clicked = !parentData.clicked;
                int differentColor = parentData.color + 50;
                avh.root.setBackgroundColor(parentData.clicked ? parentData.color : differentColor);
            });
        }

        @Override
        public AbstractExpandableItemViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
            View v = DelegatesManager.inflateMe(parent, R.layout.parent_item_1);
            return new AVH(v);
        }

        public static class AVH extends AbstractExpandableItemViewHolder {
            TextView parentName;
            ImageView imageView;
            View root;

            public AVH(View itemView) {
                super(itemView);
                root = itemView.findViewById(R.id.item_root);
                parentName = (TextView) itemView.findViewById(R.id.parent_textview);
                imageView = (ImageView) itemView.findViewById(R.id.parent_imageview);
            }
        }
    }


    public static class HeaderParentAdapter extends DelegableParentAdapter<Parent, Child> {

        @Override
        public int getParentViewType() {
            return PARENT_VIEWTYPE_2;
        }

        @Override
        public void onBindGroupViewHolder(AbstractExpandableItemViewHolder holder, final int groupPosition, int viewType) {
            final AVH avh = (AVH) holder;
            final Parent parentData = getDataSet().get(groupPosition).getParent();
            avh.root.setBackgroundColor(Color.BLACK);
            avh.parentName.setText(parentData.parentName);
            avh.itemView.setOnClickListener(v -> {
                Log.d(TAG, "onClick: ");
                parentData.clicked = !parentData.clicked;
            });
        }

        @Override
        public AbstractExpandableItemViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
            View v = DelegatesManager.inflateMe(parent, R.layout.parent_item_2);
            return new AVH(v);
        }

        public static class AVH extends AbstractExpandableItemViewHolder {
            TextView parentName;
            View root;

            public AVH(View itemView) {
                super(itemView);
                parentName = (TextView) itemView.findViewById(R.id.parent_textview);
                root = itemView.findViewById(R.id.parent_header);
                root.setBackgroundColor(Color.BLACK);
            }
        }
    }
}
