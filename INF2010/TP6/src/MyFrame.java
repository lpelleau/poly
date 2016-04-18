import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MyFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private Maze m;
	private MyPanel mazePanelContainer;
	private int height, width, seed;
	private static int DEFAULT_SEED_VALUE = 200;

	public MyFrame() {
		super("Labyrinthe");
		height = 20;
		width = 20;
		seed = DEFAULT_SEED_VALUE;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addComponentsToPane(getContentPane());
		pack();
		setVisible(true);
	}

	private void addComponentsToPane(Container pane) {

		if (!(pane.getLayout() instanceof BorderLayout)) {
			pane.add(new JLabel("Container doesn't use BorderLayout!"));
			return;
		}

		JPanel controlsp = new JPanel();

		m = new Maze(height, width, seed);
		mazePanelContainer = new MyPanel(m);

		controlsp.add(new JLabel("Graine"));
		JTextField jtf = new JTextField(String.valueOf(seed), 10);
		controlsp.add(jtf);

		// Ajouter bouton genere
		JButton generer = new JButton("G�n�rer");
		generer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int seed = DEFAULT_SEED_VALUE;
				jtf.getText();
				try {
					seed = Integer.parseInt(jtf.getText().trim());
				} catch (NumberFormatException exception) {
					jtf.setText("    " + String.valueOf(seed));
				}
				m = new Maze(height, width, seed);
				mazePanelContainer.updateMaze(m);
			}
		});
		controlsp.add(generer);

		// Ajouter bouton resoudre
		JButton resoudre = new JButton("R�soudre");

		resoudre.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				m.solve();
				mazePanelContainer.repaint();
			}
		});
		controlsp.add(resoudre);

		// ajouter tout cela au panneau principal
		pane.add(controlsp, BorderLayout.PAGE_START);
		pane.add(mazePanelContainer, BorderLayout.CENTER);
	}
}
