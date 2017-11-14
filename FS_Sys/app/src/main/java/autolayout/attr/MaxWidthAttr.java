package autolayout.attr;

import android.view.View;

import java.lang.reflect.Method;

/**
 * Created by zhy on 15/12/24.
 */
public class MaxWidthAttr extends autolayout.attr.AutoAttr
{
    public MaxWidthAttr(int pxVal, int baseWidth, int baseHeight)
    {
        super(pxVal, baseWidth, baseHeight);
    }

    @Override
    protected int attrVal()
    {
        return autolayout.attr.Attrs.MAX_WIDTH;
    }

    @Override
    protected boolean defaultBaseWidth()
    {
        return true;
    }

    @Override
    protected void execute(View view, int val)
    {
        try
        {
            Method setMaxWidthMethod = view.getClass().getMethod("setMaxWidth", int.class);
            setMaxWidthMethod.invoke(view, val);
        } catch (Exception ignore)
        {
        }
    }

    public static MaxWidthAttr generate(int val, int baseFlag)
    {
        MaxWidthAttr attr = null;
        switch (baseFlag)
        {
            case autolayout.attr.AutoAttr.BASE_WIDTH:
                attr = new MaxWidthAttr(val, autolayout.attr.Attrs.MAX_WIDTH, 0);
                break;
            case autolayout.attr.AutoAttr.BASE_HEIGHT:
                attr = new MaxWidthAttr(val, 0, autolayout.attr.Attrs.MAX_WIDTH);
                break;
            case AutoAttr.BASE_DEFAULT:
                attr = new MaxWidthAttr(val, 0, 0);
                break;
        }
        return attr;
    }

    public static int getMaxWidth(View view)
    {
        try
        {
            Method setMaxWidthMethod = view.getClass().getMethod("getMaxWidth");
            return (int) setMaxWidthMethod.invoke(view);
        } catch (Exception ignore)
        {
        }
        return 0;
    }
}
