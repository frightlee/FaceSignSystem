package autolayout.attr;

import android.view.View;

/**
 * Created by zhy on 15/12/5.
 */
public class PaddingBottomAttr extends autolayout.attr.AutoAttr
{
    public PaddingBottomAttr(int pxVal, int baseWidth, int baseHeight)
    {
        super(pxVal, baseWidth, baseHeight);
    }

    @Override
    protected int attrVal()
    {
        return autolayout.attr.Attrs.PADDING_BOTTOM;
    }

    @Override
    protected boolean defaultBaseWidth()
    {
        return false;
    }

    @Override
    protected void execute(View view, int val)
    {
        int l = view.getPaddingLeft();
        int t = view.getPaddingTop();
        int r = view.getPaddingRight();
        int b = val;
        view.setPadding(l, t, r, b);

    }


    public static PaddingBottomAttr generate(int val, int baseFlag)
    {
        PaddingBottomAttr attr = null;
        switch (baseFlag)
        {
            case autolayout.attr.AutoAttr.BASE_WIDTH:
                attr = new PaddingBottomAttr(val, autolayout.attr.Attrs.PADDING_BOTTOM, 0);
                break;
            case autolayout.attr.AutoAttr.BASE_HEIGHT:
                attr = new PaddingBottomAttr(val, 0, autolayout.attr.Attrs.PADDING_BOTTOM);
                break;
            case AutoAttr.BASE_DEFAULT:
                attr = new PaddingBottomAttr(val, 0, 0);
                break;
        }
        return attr;
    }
}
