package autolayout.attr;

import android.view.View;

/**
 * Created by zhy on 15/12/5.
 */
public class PaddingRightAttr extends autolayout.attr.AutoAttr
{
    public PaddingRightAttr(int pxVal, int baseWidth, int baseHeight)
    {
        super(pxVal, baseWidth, baseHeight);
    }

    @Override
    protected int attrVal()
    {
        return autolayout.attr.Attrs.PADDING_RIGHT;
    }

    @Override
    protected boolean defaultBaseWidth()
    {
        return true;
    }

    @Override
    protected void execute(View view, int val)
    {
        int l = view.getPaddingLeft();
        int t = view.getPaddingTop();
        int r = val;
        int b = view.getPaddingBottom();
        view.setPadding(l, t, r, b);

    }


    public static PaddingRightAttr generate(int val, int baseFlag)
    {
        PaddingRightAttr attr = null;
        switch (baseFlag)
        {
            case autolayout.attr.AutoAttr.BASE_WIDTH:
                attr = new PaddingRightAttr(val, autolayout.attr.Attrs.PADDING_RIGHT, 0);
                break;
            case autolayout.attr.AutoAttr.BASE_HEIGHT:
                attr = new PaddingRightAttr(val, 0, autolayout.attr.Attrs.PADDING_RIGHT);
                break;
            case AutoAttr.BASE_DEFAULT:
                attr = new PaddingRightAttr(val, 0, 0);
                break;
        }
        return attr;
    }
}
