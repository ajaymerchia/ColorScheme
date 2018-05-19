import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Arrays;

public class ColorScheme
{
	JFrame frame;
  JTextField add_color = new JTextField("Enter a Hex Code and hit enter (omit #). Type \'toggle lines\' to remove the rectangle lines. Click on two rectangles to swap.");
	ColorPalette panel;
  int FRAME_WIDTH = 1080;
  int FRAME_HEIGHT = 720;
  int NUM_PANES =  5;
  Color[] colors = new Color[NUM_PANES];
  String[] hex_vals = new String[5];
  boolean default_State = true;
  boolean draw_lines = true;

	public static void main(String[] args)
	{
		ColorScheme pw = new ColorScheme();
		pw.Run();
	}

  public void defaultColors()
  {
    colors[0] = Color.red;
    colors[1] = Color.orange;
    colors[2] = Color.yellow;
    colors[3] = Color.green;
    colors[4] = Color.blue;

    for (int i = 0; i < colors.length; i++)
      hex_vals[i] = String.format("#%02x%02x%02x", colors[i].getRed(), colors[i].getGreen(), colors[i].getBlue());

  }

	public void Run()
	{
    defaultColors();
		frame = new JFrame("ColorScheme");
		frame.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                // This is only called when the user releases the mouse button.
								FRAME_WIDTH = frame.getWidth();
								FRAME_HEIGHT = frame.getHeight();
								panel.repaint();
            }
        });
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create JPanel and add to frame
		panel = new ColorPalette();

    add_color.addKeyListener(new KeyListener(){
      public void keyPressed(KeyEvent keyEvent) {
        if (keyEvent.getKeyCode()==KeyEvent.VK_ENTER){
          if (!add_color.getText().equals("toggle lines"))
          {
            addColor();
          }else
            draw_lines = !draw_lines;
            panel.repaint();
        }
     }

     public void keyReleased(KeyEvent keyEvent) {}

     public void keyTyped(KeyEvent keyEvent) {}

     public void addColor(){
       if (default_State)
       {
         default_State = !default_State;
         NUM_PANES = 0;
         hex_vals = new String[0];
       }
       NUM_PANES++;
       String hex_code = add_color.getText();
       hex_code = "#" + hex_code;
       String[] newHexes = new String[hex_vals.length+1];

       //move all the colors from the original to this for
       for (int i = 0; i < hex_vals.length; i++)
       {
         newHexes[i] = hex_vals[i];
       }
       newHexes[newHexes.length-1] = hex_code;
       hex_vals = newHexes;
       hex_vals = hex_sort(hex_vals);

       //update colors
       colors = new Color[hex_vals.length];

       for (int i = 0; i < colors.length; i++)
       {
         colors[i] = Color.decode(hex_vals[i]);
       }

       panel.repaint();

     }
     public void printIArray(int [] arr){
       for (int i = 0; i < arr.length; i++)
       {
         System.out.println(arr[i]);
       }}
     public void printSArray(String [] arr){
       for (int i = 0; i < arr.length; i++)
       {
         System.out.println(arr[i]);
       }}

     public String[] hex_sort(String[] hex_vals)
     {
       int[] hex_Dec = new int[hex_vals.length];

       for (int i = 0; i < hex_vals.length; i++)
       {
         int r = hex2decimal(hex_vals[i].substring(1,3));
         int g = hex2decimal(hex_vals[i].substring(3,5));
         int b = hex2decimal(hex_vals[i].substring(5,7));
         hex_Dec[i] = r + g + b;
       }

       int[] sorted_hex_Dec = new int[hex_Dec.length];
       for (int i = 0; i < hex_Dec.length; i++)
          sorted_hex_Dec[i] = hex_Dec[i];

       Arrays.sort(sorted_hex_Dec);



       String[] sortedHexes = new String[hex_vals.length];

       for (int hexI = 0; hexI < sorted_hex_Dec.length; hexI++)
          for(int origPos = 0; origPos < hex_Dec.length; origPos++)
              if(sorted_hex_Dec[hexI] == hex_Dec[origPos])
                  sortedHexes[hexI] = hex_vals[origPos];

      return sortedHexes;

     }

     public int hex2decimal(String s) {
            String digits = "0123456789ABCDEF";
            s = s.toUpperCase();
            int val = 0;
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                int d = digits.indexOf(c);
                val = 16*val + d;
            }
            return val;
        }

   });

		frame.getContentPane().add(panel, BorderLayout.CENTER);	// add panel to frame
    //frame.getContentPane().add(add_pane, BorderLayout.NORTH);
    frame.getContentPane().add(add_color,BorderLayout.SOUTH);

		frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);		// explicitly set size in pixels
		frame.setVisible(true);		// set to false to make invisible
	}

  class ColorPalette extends JPanel implements MouseListener
  {

    boolean color_selected = false;
    int oriRec, desRec;
    int usable_width,pane_width;

  	ColorPalette()
  	{
  		setBackground(Color.white);
      this.addMouseListener(this);
  	}

  	public void paintComponent(Graphics g)
  	{
  		super.paintComponent(g);

      usable_width = FRAME_WIDTH - 200;
      pane_width = usable_width/NUM_PANES;

      for (int i = 0; i < NUM_PANES; i++){
          g.setColor(colors[i]);
  		    g.fillRect(i*pane_width+100,50,pane_width,FRAME_HEIGHT-150);
          g.setColor(Color.black);
          if(draw_lines)
            g.drawRect(i*pane_width+100,50,pane_width,FRAME_HEIGHT-150);
          g.drawString(hex_vals[i],i*pane_width+100,FRAME_HEIGHT-75);

          if(color_selected && i == oriRec)
            drawRectangle(g,i*pane_width+100,50,pane_width,FRAME_HEIGHT-150,5);
      }

      //if rectangle selected, draw a bright border

  	}

    public void drawRectangle(Graphics g, int x, int y, int width, int height, int thickness) {
      g.drawRect(x, y, width, height);
      if (thickness > 1)
        drawRectangle(g, x + 1, y + 1, width - 2, height - 2, thickness - 1);

    }

    public int rectIndex(int x, int y)
    {

      int index = (x-100)/pane_width;


      return index;

    }

    public void mouseClicked ( MouseEvent e ){}

  	public void mousePressed ( MouseEvent e )
    {

      int xPos = e.getX();
      int yPos = e.getY();


      //If choosing origin color
      if (!color_selected)
        oriRec = rectIndex(xPos,yPos);
      else{
        desRec = rectIndex(xPos,yPos);
        Color swap_inst = colors[desRec];
        colors[desRec] = colors[oriRec];
        colors[oriRec] = swap_inst;

        String inter = hex_vals[desRec];
        hex_vals[desRec] = hex_vals[oriRec];
        hex_vals[oriRec] = inter;
      }


      color_selected = !color_selected;
      repaint();
  	}

  	public void mouseReleased ( MouseEvent e ){}

  	public void mouseEntered ( MouseEvent e ){}

  	public void mouseExited ( MouseEvent e ){}

  }
}
