package autolayout.attr;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zhy on 15/12/5.
 */
public class MarginTopAttr extends autolayout.attr.AutoAttr
{
    public MarginTopAttr(int pxVal, int baseWidth, int baseHeight)
    {
        super(pxVal, baseWidth, baseHeight);
    }

    @Override
    protected int attrVal()
    {
        return autolayout.attr.Attrs.MARGIN_TOP;
    }

    @Override
    protected boolean defaultBaseWidth()
    {
        return false;
    }

    @Override
    protected void execute(View view, int val)
    {
        if (!(view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams))
        {
            return;
        }
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        lp.topMargin = val;

    }


    public static MarginTopAttr generate(int val, int baseFlag)
    {
        MarginTopAttr attr = null;
        switch (baseFlag)
        {
            case autolayout.attr.AutoAttr.BASE_WIDTH:
                attr = new MarginTopAttr(val, autolayout.attr.Attrs.MARGIN_TOP, 0);
                break;
            case autolayout.attr.AutoAttr.BASE_HEIGHT:
                attr = new MarginTopAttr(val, 0, autolayout.attr.Attrs.MARGIN_TOP);
                break;
            case AutoAttr.BASE_DEFAULT:
                attr = new MarginTopAttr(val, 0, 0);
                break;
        }
        return attr;
    }
}
