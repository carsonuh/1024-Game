package pack1024;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class GUI implements KeyEventDispatcher, ActionListener
{

	private final static int DEFAULT_SIZE = 4;
	private JFrame top;
	private JPanel gamePanel;
	private JPanel scorePanel;
	private JPanel score;
	private JPanel gameContainer;
	private JLabel[][] gameBoardUI;
	private JMenuItem reset, quit;
	private JLabel scoreLabel;
	private JLabel scoreTxt;
	private Font tileFont;
	private JButton undo;
	private int[][] test = new int[4][4];
	private NumberGame game;

	public GUI()
	{
		test[0][0] = 2;
		test[0][1] = 2;
		test[0][2] = 4;
		test[0][3] = 4;
		test[1][0] = 8;
		test[1][1] = 16;
		test[1][2] = 32;
		test[1][3] = 64;
		test[2][0] = 128;
		test[2][1] = 256;
		test[2][2] = 512;
		test[2][3] = 1024;
		
		top = new JFrame("1024");
		
		game = new NumberGame();
		game.resizeBoard(DEFAULT_SIZE, DEFAULT_SIZE, 1024);

		
		Border border = BorderFactory.createLineBorder(new Color(83, 83, 83), 5);
		Border padding = BorderFactory.createEmptyBorder(15, 15, 15, 15);
		Border border2 = BorderFactory.createLineBorder(new Color(83, 83, 83), 3);
		
		// game panel -------------------------------------------------------------
		gamePanel = new JPanel();
		gamePanel.setLayout(new GridLayout(DEFAULT_SIZE, DEFAULT_SIZE));
		gamePanel.setBorder(border);

		
		
		// game container panel ---------------------------------------------------
		gameContainer = new JPanel();
		gameContainer.setLayout(new BorderLayout());
		gameContainer.setBorder(border2);
		gameContainer.add(gamePanel, BorderLayout.CENTER);

		// container panel --------------------------------------------------------
		JPanel container = new JPanel();
		container.setLayout(new BorderLayout());

		// score panel ------------------------------------------------------------
		scorePanel = new JPanel();
		scorePanel.setLayout(new BorderLayout());
		scorePanel.setBackground(new Color(49, 49, 49));
		scorePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
		scoreLabel = new JLabel();
		scoreTxt = new JLabel("Score: ");
		undo = new JButton("Undo");

		scorePanel.add(undo, BorderLayout.WEST);
		
		JPanel score = new JPanel();
		score.setLayout(new BorderLayout());
		score.setBackground(new Color(49, 49, 49));
		score.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
		score.add(scoreTxt, BorderLayout.WEST);
		score.add(scoreLabel, BorderLayout.EAST);
		
		scorePanel.add(score, BorderLayout.EAST);
		container.add(scorePanel, BorderLayout.NORTH);
		container.add(gameContainer, BorderLayout.SOUTH);

		container.setBorder(padding);
		container.setBackground(new Color(49, 49, 49));
		top.add(container);

		KeyboardFocusManager kManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		kManager.addKeyEventDispatcher(this);

		gameBoardUI = new JLabel[DEFAULT_SIZE][DEFAULT_SIZE];
		tileFont = new Font(Font.SERIF, Font.BOLD, 45);
		for (int k = 0; k < gameBoardUI.length; k++)
			for (int m = 0; m < gameBoardUI[k].length; m++)
			{
				gameBoardUI[k][m] = new JLabel("", JLabel.CENTER);
				gameBoardUI[k][m].setFont(getFont(game.board[k][m]));
				gameBoardUI[k][m].setPreferredSize(new Dimension(100, 100));
				gameBoardUI[k][m].setBackground(new Color(255, 255, 255));
				gameBoardUI[k][m].setBorder(border);
				gameBoardUI[k][m].setOpaque(true);
				gamePanel.add(gameBoardUI[k][m]);
			}
		
		game.placeRandomValue();
		game.placeRandomValue();
		
		// menu ------------------------------------------------------------
		JMenuBar mb = new JMenuBar();
		top.setJMenuBar(mb);
		JMenu game = new JMenu("Game");
		mb.add(game);
		reset = new JMenuItem("Reset");
		reset.addActionListener(this);
		quit = new JMenuItem("Quit");
		quit.addActionListener(this);
		game.add(reset);
		game.addSeparator();
		game.add(quit);

		undo.addActionListener(this);
		
		style();
		updateUI();
		
		top.pack();
		top.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		top.setSize(460, 533);
		top.setResizable(false);
		top.setVisible(true);
	}

	public static void main(String[] args)
	{
		new GUI();
	}

	private void updateUI()
	{
		for (int k = 0; k < gameBoardUI.length; k++)
		{
			for (int m = 0; m < gameBoardUI[k].length; m++)
			{
				gameBoardUI[k][m].setText("");
				gameBoardUI[k][m].setBackground(new Color(252,252,252));
			}
		}

		for (Cell c : game.getNonEmptyTiles())
		{
			gameBoardUI[c.row][c.column].setText("" + c.value);
			gameBoardUI[c.row][c.column].setBackground(getColor(c.value));
			gameBoardUI[c.row][c.column].setFont(getFont(c.value));
			gameBoardUI[c.row][c.column].setOpaque(true);

			if (c.value == 2 || c.value == 4)
			{
				gameBoardUI[c.row][c.column].setForeground(new Color(83, 83, 83));
			}
			else
			{
				gameBoardUI[c.row][c.column].setForeground(new Color(255, 255, 255));
			}
		}
		scoreLabel.setText("" + game.getScore());
	}

	public Font getFont(int value)
	{
		if (value >= 0 && value < 100)
		{
			return new Font(Font.SANS_SERIF, Font.BOLD, 45);
		}

		else if (value >= 100 && value < 1000)
		{
			return new Font(Font.SANS_SERIF, Font.BOLD, 40);
		}
		else
		{
			return new Font(Font.SANS_SERIF, Font.BOLD, 35);
		}
	}

	public Color getColor(int value)
	{
		switch (value)
		{
		case 2:
			return new Color(228, 234, 213);
		case 4:
			return new Color(194, 196, 187);
		case 8:
			return new Color(251, 186, 77);
		case 16:
			return new Color(242, 127, 53);
		case 32:
			return new Color(248, 108, 72);
		case 64:
			return new Color(246, 80, 38);
		case 128:
			return new Color(100, 195, 150);
		case 256:
			return new Color(70, 192, 200);
		case 512:
			return new Color(39, 145, 206);
		case 1024:
			return new Color(168, 158, 206);
		default:
			return new Color(228, 234, 213);
		}
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent e)
	{
		if (e.getID() == KeyEvent.KEY_PRESSED)
		{
			switch (e.getKeyCode())
			{
			case KeyEvent.VK_UP:
				game.slide(SlideDirection.UP);
				break;
			case KeyEvent.VK_DOWN:
				game.slide(SlideDirection.DOWN);
				break;
			case KeyEvent.VK_LEFT:
				game.slide(SlideDirection.LEFT);
				break;
			case KeyEvent.VK_RIGHT:
				game.slide(SlideDirection.RIGHT);
				break;
			case KeyEvent.VK_U:
				game.undo();
				updateUI();
				break;
			}

			updateUI();

			if (game.getStatus() == GameStatus.USER_WON)
			{
				JOptionPane.showMessageDialog(null, "You Won!");
			}

			if (game.getStatus() == GameStatus.USER_LOST)
			{
				int response = JOptionPane.showConfirmDialog(null, "Do you want to play again?", "GAME OVER!",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

				if (response == JOptionPane.NO_OPTION)
				{
					System.exit(0);
				}
				else if (response == JOptionPane.YES_OPTION)
				{
					game.reset();
					updateUI();
				}
			}
		}
		return true;
	}

	public void style()
	{
		Font font = new Font(Font.SANS_SERIF, Font.BOLD, 15);
		Border border = BorderFactory.createLineBorder(new Color(135, 118, 100), 0);
		
		undo.setForeground(new Color(241, 77, 84));
		undo.setBackground(new Color(49, 49, 49));
		undo.setFont(font);
		undo.setOpaque(true);
		undo.setPreferredSize(new Dimension(75, 30));
		undo.setFocusable(false);
		undo.setBorder(border);
		
		scoreLabel.setForeground(new Color(241, 77, 84));
		scoreLabel.setPreferredSize(new Dimension(40, 25));
		scoreLabel.setFont(font);
		
		scoreTxt.setForeground(new Color(241, 77, 84));
		scoreTxt.setFont(font);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object which = e.getSource();
		if (which == reset)
		{
			game.reset();
			updateUI();
		}

		if (which == quit)
		{
			System.exit(0);
		}

		if (which == undo)
		{
			game.undo();
			updateUI();
		}
	}
}
