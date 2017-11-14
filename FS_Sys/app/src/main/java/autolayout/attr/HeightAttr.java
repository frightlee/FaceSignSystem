package autolayout.attr;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zhy on 15/12/5.
 */
public class HeightAttr extends autolayout.attr.AutoAttr
{
    public HeightAttr(int pxVal, int baseWidth, int baseHeight)
    {
        super(pxVal, baseWidth, baseHeight);
    }

    @Override
    protected int attrVal()
    {
        return autolayout.attr.Attrs.HEIGHT;
    }

    @Override
    protected boolean defaultBaseWidth()
    {
        return false;
    }

    @Override
    protected void execute(View view, int val)
    {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        lp.height = val;
    }

    public static HeightAttr generate(int val, int baseFlag)
    {
        HeightAttr heightAttr = null;
        switch (baseFlag)
        {
            case autolayout.attr.AutoAttr.BASE_WIDTH:
                heightAttr = new HeightAttr(val, autolayout.attr.Attrs.HEIGHT, 0);
                break;
            case autolayout.attr.AutoAttr.BASE_HEIGHT:
                heightAttr = new HeightAttr(val, 0, autolayout.attr.Attrs.HEIGHT);
                break;
            case AutoAttr.BASE_DEFAULT:
                heightAttr = new HeightAttr(val, 0, 0);
                break;
        }
        return heightAttr;
    }


}
