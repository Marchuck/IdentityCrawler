package pl.marczak.identitycrawler;

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
import rx.functions.Func0;

/**
 * @author Lukasz Marczak
 * @since 04.06.16.
 */
public class ExpandableWrapper {
    public static final String TAG = ExpandableWrapper.class.getSimpleName();
    public static final String ANONYMOUS = "Anonymous";
    public static final int PARENT_VIEWTYPE_1 = 1;
    public static final int PARENT_VIEWTYPE_2 = 2;

    public static final int CHILD_VIEWTYPE = 3;
    public static final int CHILD_VIEWTYPE_2 = 4;

    public static Observable<RecyclerView.Adapter> getAdapter() {
        return Observable.defer(new Func0<Observable<RecyclerView.Adapter>>() {
            @Override
            public Observable<RecyclerView.Adapter> call() {
                return Observable.just(createAdapter());
            }
        });
    }

    private static RecyclerView.Adapter createAdapter() {
        DelegatesManager<Parent, Child> manager = new DelegatesManager<>();
        manager.addDelegateChild(new ChildAdapter());
        manager.addDelegateChild(new ExtendedChildAdapter());
        manager.addDelegateParent(new ParentAdapter());
        manager.addDelegateParent(new YetAnotherParentAdapter());

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


        dataSet.add(new XParent<>(new Parent("Friday", R.drawable.waves), children2, PARENT_VIEWTYPE_1));
        dataSet.add(new XParent<>(new Parent("Monday", R.drawable.cool), children2, PARENT_VIEWTYPE_2));
        dataSet.add(new XParent<>(new Parent("Saturday", R.drawable.smallimage), children, PARENT_VIEWTYPE_2));
        dataSet.add(new XParent<>(new Parent("Weekend", R.drawable.waves), children3, PARENT_VIEWTYPE_1));


        DelegatesAdapter<Parent, Child> delegatesAdapter = new DelegatesAdapter<>(dataSet, manager);
        return delegatesAdapter;
    }

    public static class Parent {
        public boolean clicked;
        public int res;

        public Parent(String parentName, int res) {
            this.parentName = parentName;
            this.res = res;

        }

        public String parentName;
    }

    public static class Child {
        public String childName;
        public int backgroundColor;
        public int res;

        public Child(String childName, int res) {
            this.childName = childName;
            this.res = res;
        }
    }

    public static class ChildAdapter extends DelegableChildAdapter<Parent, Child> {


        @Override
        public int getChildViewType() {
            return CHILD_VIEWTYPE;
        }

        @Override
        public void onBindChildViewHolder(RecyclerView.ViewHolder holder, final int groupPosition,
                                          final int childPosition, int viewType) {
            XChild<Child> childWrapper = getDataSet().get(groupPosition).getChildren().get(childPosition);
            ChildViewHolder vh = (ChildViewHolder) holder;
            vh.textView.setText(childWrapper.getChild().childName);
            vh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        @Override
        public RecyclerView.ViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
            View v = DelegatesManager.inflateMe(parent, R.layout.child_item_1);
            return new ChildViewHolder(v);
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
            ExtendedChildViewHolder vh = (ExtendedChildViewHolder) holder;
            vh.rootView.setBackgroundColor(childWrapper.getChild().backgroundColor);
            vh.textView.setImageResource(childWrapper.getChild().res);
        }

        @Override
        public RecyclerView.ViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
            View v = DelegatesManager.inflateMe(parent, R.layout.child_item_2);
            return new ExtendedChildViewHolder(v);
        }
    }

    public static class ChildViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        View rootView;

        public ChildViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            textView = (TextView) rootView.findViewById(R.id.child_textView);
        }
    }

    public static class ExtendedChildViewHolder extends RecyclerView.ViewHolder {
        ImageView textView;
        View rootView;

        public ExtendedChildViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            textView = (ImageView) rootView.findViewById(R.id.child_imageView2);
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
            avh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: ");
                    parentData.clicked = !parentData.clicked;
                    avh.imageView.setImageResource(parentData.clicked ?
                            android.R.drawable.arrow_up_float : android.R.drawable.arrow_down_float);
                }
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

            public AVH(View itemView) {
                super(itemView);
                parentName = (TextView) itemView.findViewById(R.id.parent_textview);
                imageView = (ImageView) itemView.findViewById(R.id.parent_imageview);
            }
        }
    }


    public static class YetAnotherParentAdapter extends DelegableParentAdapter<Parent, Child> {


        @Override
        public int getParentViewType() {
            return PARENT_VIEWTYPE_2;
        }

        @Override
        public void onBindGroupViewHolder(AbstractExpandableItemViewHolder holder, final int groupPosition, int viewType) {
            final AVH avh = (AVH) holder;
            final Parent parentData = getDataSet().get(groupPosition).getParent();
            avh.parentName.setText(parentData.parentName);
            avh.imageView2.setImageResource(parentData.res);
            avh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: ");
                    parentData.clicked = !parentData.clicked;
                    avh.imageView.setImageResource(parentData.clicked ?
                            android.R.drawable.arrow_up_float : android.R.drawable.arrow_down_float);
                }
            });
        }

        @Override
        public AbstractExpandableItemViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
            View v = DelegatesManager.inflateMe(parent, R.layout.parent_item_2);
            return new AVH(v);
        }

        public static class AVH extends AbstractExpandableItemViewHolder {
            TextView parentName;
            ImageView imageView, imageView2;

            public AVH(View itemView) {
                super(itemView);
                parentName = (TextView) itemView.findViewById(R.id.parent_textview);
                imageView = (ImageView) itemView.findViewById(R.id.parent_imageview);
                imageView2 = (ImageView) itemView.findViewById(R.id.parent_imageview_2);
            }
        }
    }
}
