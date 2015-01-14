import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.peer.*;
import sun.awt.*;

public class MyWindow extends Canvas
{
    static 
    {
        // Load the library that contains the JNI code.
        System.loadLibrary("MyWindow");
    }

    // native entry point for initializing the IE control.
    public native void initialize(int hwnd, String strURL);

    // native entry point for resizing
    public native void resizeControl(int hwnd, int nWidth, int nHeight);

    // Returns the HWND for panel. This is a hack which works with
    // JDK1.1.8, JDK1.2.2 and JDK1.3. This is undocumented. Also 
    // you should call this only after addNotify has been called.
    public int getHWND() 
    {
        int hwnd = 0;
        DrawingSurfaceInfo drawingSurfaceInfo = ((DrawingSurface)(getPeer())).getDrawingSurfaceInfo();
        if (null != drawingSurfaceInfo) 
		{
            drawingSurfaceInfo.lock();
            Win32DrawingSurface win32DrawingSurface = (Win32DrawingSurface)drawingSurfaceInfo.getSurface();
            hwnd = win32DrawingSurface.getHWnd();
            drawingSurfaceInfo.unlock();
        }
        return hwnd;
    }
    
    public void addNotify()
    {
        super.addNotify();
        m_hWnd = getHWND();
        initialize(m_hWnd, m_strURL);
    }

    String m_strURL = "http://www.javasoft.com";
    int    m_hWnd   = 0;
	
    public static void main( String[] argv )
    {
        Frame f = new Frame();
        f.setLayout(new BorderLayout());
        f.setTitle("Internet Explorer inside Java Canvas");
		
        MyWindow w = new MyWindow();
        if(argv.length>0)
            w.m_strURL = argv[0];

        String strText = "URL:" + w.m_strURL;
        f.add(w,BorderLayout.CENTER);
        f.add(new Label(strText),BorderLayout.NORTH);
        f.setBounds(300,300,500,300);
        f.setVisible(true);
    }

    public void setSize( int width, int height ) 
    {
        super.setSize(width,height);
        if(m_hWnd!=0)
            resizeControl(m_hWnd, width, height);
    }

    public void setSize( Dimension d ) 
    {
        super.setSize(d);
        if(m_hWnd!=0)
            resizeControl(m_hWnd, d.width, d.height);
    }

    public void setBounds( int x, int y, int width, int height ) 
    {
        super.setBounds(x,y,width,height);
        if(m_hWnd!=0)
            resizeControl(m_hWnd, width, height);
    }

    public void setBounds( Rectangle r ) 
    {
        super.setBounds(r);
        if(m_hWnd!=0)
            resizeControl(m_hWnd, r.width, r.height);
    }
}
