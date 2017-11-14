package autolayout.attr;

import android.view.View;

/**
 * Created by zhy on 15/12/5.
 */
public class PaddingTopAttr extends autolayout.attr.AutoAttr
{
    public PaddingTopAttr(int pxVal, int baseWidth, int baseHeight)
    {
        super(pxVal, baseWidth, baseHeight);
    }

    @Override
    protected int attrVal()
    {
        return autolayout.attr.Attrs.PADDING_TOP;
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
        int t = val;
        int r = view.getPaddingRight();
        int b = view.getPaddingBottom();
        view.setPadding(l, t, r, b);
    }

    public static PaddingTopAttr generate(int val, int baseFlag)
    {
        PaddingTopAttr attr = null;
        switch (baseFlag)
        {
            case autolayout.attr.AutoAttr.BASE_WIDTH:
                attr = new PaddingTopAttr(val, autolayout.attr.Attrs.PADDING_TOP, 0);
                break;
            case autolayout.attr.AutoAttr.BASE_HEIGHT:
                attr = new PaddingTopAttr(val, 0, autolayout.attr.Attrs.PADDING_TOP);
                break;
            case AutoAttr.BASE_DEFAULT:
                attr = new PaddingTopAttr(val, 0, 0);
                break;
        }
        return attr;
    }
}
