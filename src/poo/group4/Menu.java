package poo.group4;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import poo.group4.patterns.Pattern;
import poo.group4.patterns.TestSet;
import poo.group4.patterns.TrainSet;

/**
 * A Graphical User Interface for the Bayesian Classifier project. Provides an easy way to load train and test files, choose the score model,
 * construct the classifier, and classify the test set. Shows the time spent building the classifier, with and without file loading. Optionally saves
 * the output to a file. Provides means to save and load the classifier for testing at a different time.
 * 
 * @author Group 4
 * 
 */
public class Menu extends JFrame {

	private static final long serialVersionUID = -3428436893641809391L;

	private JPanel contentPane;
	JButton trainFileButton;
	JButton testFileButton;
	JFileChooser fChooser;
	private JTextArea textAreaLog;
	JLabel lblTimer;
	File trainFile;
	File testFile;
	String scoreModelSelected;
	TreeAugmentedNaiveBayesClassifier classifier;
	float timerWithFileReading;
	float timerWithoutFileReading;
	boolean timerHasFileReadingTime;
	TrainSet trainData;
	private JCheckBox chckbxSaveOutputTo;

	/**
	 * Launch the GUI application for the OOP TAN Bayes Classifier.
	 */
	public static void startGui() {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Menu frame = new Menu();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Menu() {
		super("Bayesian Classifier - OOP Project");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 533, 407);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		textAreaLog = new JTextArea();
		textAreaLog.setFont(new Font("Monospaced", Font.PLAIN, 13));
		textAreaLog.setEditable(false);

		JScrollPane logScrollPane = new JScrollPane(textAreaLog);
		logScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		final JLabel trainFileLabel = new JLabel("Please choose a csv file");
		trainFileLabel.setForeground(Color.red);
		trainFileLabel.setBackground(Color.white);
		trainFileLabel.setOpaque(true);
		final JLabel testFileLabel = new JLabel("Please choose a csv file");
		testFileLabel.setForeground(Color.red);
		testFileLabel.setBackground(Color.white);
		testFileLabel.setOpaque(true);

		fChooser = new JFileChooser();
		final FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV files (*.csv)", "csv");
		fChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));

		trainFileButton = new JButton("Open Train File");
		trainFileButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fChooser.setFileFilter(filter);
				int returnVal = fChooser.showOpenDialog(contentPane); // nao sei o Menu.this faz ai dentro! (Menu.this e a janela principal?!)

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					trainFile = fChooser.getSelectedFile();

					textAreaLog.append("Opening Test File: " + trainFile.getName() + ".\n");
					trainFileLabel.setText("Chosen file: " + trainFile.getName());
					trainFileLabel.setForeground(Color.black);
					trainFileLabel.setOpaque(false);

				} else {
					textAreaLog.append("Open Test File command cancelled by user.\n");
				}
				textAreaLog.setCaretPosition(textAreaLog.getDocument().getLength()); // nao sei o que faz!

			}
		});

		testFileButton = new JButton("Open Test File");
		testFileButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				fChooser.setFileFilter(filter);
				int returnVal = fChooser.showOpenDialog(contentPane); // nao sei o Menu.this faz ai dentro! (Menu.this e a janela principal?!)

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					testFile = fChooser.getSelectedFile();

					textAreaLog.append("Opening Test File: " + testFile.getName() + ".\n");
					testFileLabel.setText("Chosen file: " + testFile.getName());
					testFileLabel.setForeground(Color.black);
					testFileLabel.setOpaque(false);
				} else {
					textAreaLog.append("Open Test File command cancelled by user.\n");
				}
				textAreaLog.setCaretPosition(textAreaLog.getDocument().getLength()); // nao sei o que faz!

			}
		});

		JSeparator separator = new JSeparator();
		JSeparator separator_1 = new JSeparator();
		JSeparator separator_2 = new JSeparator();

		lblTimer = new JLabel("Timer");
		lblTimer.setBackground(Color.BLACK);
		lblTimer.setForeground(Color.WHITE);
		lblTimer.setOpaque(true);
		lblTimer.setHorizontalAlignment(SwingConstants.CENTER);
		lblTimer.setVerticalAlignment(SwingConstants.CENTER);

		JButton btnConstructClassifier = new JButton("Construct Classifier");
		btnConstructClassifier.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (trainFile == null) {
					textAreaLog.append("Please select a Train File before executing 'Construct Classifier'.\n");
				} else {
					textAreaLog.append("Constructing Classifier using " + scoreModelSelected + " Score Model.\n");
					long startTimeBeforeReadingFile = System.currentTimeMillis();
					try {
						trainData = new TrainSet(trainFile);
					} catch (Exception e) {
						JOptionPane.showMessageDialog(contentPane, "Error reading train set:\n\n" + e, "Error reading train set",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					long startTimeAfterReadingFile = System.currentTimeMillis();

					ScoreModel scoreModel = null;
					if ("LL".equals(scoreModelSelected)) {
						scoreModel = new LogLikelihoodModel();
					} else if ("MDL".equals(scoreModelSelected)) {
						scoreModel = new MinimumDescriptionLengthModel();
					} else { // Unnecessary
						System.err.println("Score argument must be LL or MDL.");
						System.exit(1);
					}

					try {
						classifier = new TreeAugmentedNaiveBayesClassifier(trainData, scoreModel);
					} catch (InconsistentGraphException e1) {
						JOptionPane.showMessageDialog(contentPane, "Error building classifier:\n\n" + e1, "Error building classifier",
								JOptionPane.ERROR_MESSAGE);
						return;
					}

					long endTime = System.currentTimeMillis();

					timerWithFileReading = (float) (endTime - startTimeBeforeReadingFile) / 1000;
					timerWithoutFileReading = (float) (endTime - startTimeAfterReadingFile) / 1000;

					if (timerHasFileReadingTime) {
						lblTimer.setText(String.valueOf(timerWithFileReading) + " seconds");
					} else {
						lblTimer.setText(String.valueOf(timerWithoutFileReading) + " seconds");
					}
					textAreaLog.append("Classifier constructed successfully.\n");
				}
			}
		});

		JButton btnClassifyTestFile = new JButton("Classify Test File");
		btnClassifyTestFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (classifier == null) {
					textAreaLog.append("Please construct a classifier before executing 'Classify Test File'.\n");
				} else if (testFile == null) {
					textAreaLog.append("Please select a Test File before executing 'Classify Test File'.\n");
				} else {
					TestSet testData = null;

					try {
						testData = new TestSet(testFile, trainData);
					} catch (Exception e) {
						JOptionPane.showMessageDialog(contentPane, "Error during classification:\n\n" + e, "Error during classification",
								JOptionPane.ERROR_MESSAGE);
						return;
					}

					classifier.classify(testData);
					textAreaLog.append("File classified successfully.\n");

					// Output as specified in the project statement

					StringBuilder sb = new StringBuilder();
					int i = 0;
					for (Pattern tp : testData.getPatterns()) {

						if (chckbxSaveOutputTo.isSelected())
							sb.append("-> " + tp.toString(true) + "\n");
						else
							sb.append("-> instance " + i + ": " + tp.getOutput() + "\n");

						i++;
					}

					if (chckbxSaveOutputTo.isSelected()) {
						int ret = fChooser.showSaveDialog(contentPane);

						if (ret == JFileChooser.APPROVE_OPTION) {
							try {
								File file = fChooser.getSelectedFile();
								PrintStream ps = new PrintStream(new FileOutputStream(file));

								ps.append(sb.toString());
								textAreaLog.append("Output saved to " + file.getName());

								ps.close();

							} catch (FileNotFoundException e) {
								textAreaLog.append("Error: " + e.toString());
							}

						}
					} else {
						textAreaLog.append(sb.toString());
					}
				}
			}
		});

		final String[] comboBoxItems = { "LL", "MDL" };
		scoreModelSelected = comboBoxItems[0];
		// This should not be a raw generic, but the current WindowBuilder version does not support this as generic
		final JComboBox scoreModelComboBox = new JComboBox(comboBoxItems);
		scoreModelComboBox.addItemListener(new ItemListener() {

			public void itemStateChanged(ItemEvent e) {

				if (e.getStateChange() == ItemEvent.SELECTED)
					scoreModelSelected = comboBoxItems[scoreModelComboBox.getSelectedIndex()];
			}
		});

		JButton btnSaveClassifier = new JButton("Save Classifier");
		btnSaveClassifier.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (classifier == null) {
					textAreaLog.append("Please construct a classifier before trying to save.\n");
				} else {
					int returnVal = fChooser.showSaveDialog(contentPane);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						File file = fChooser.getSelectedFile();

						try {
							classifier.saveClassifier(file);
							textAreaLog.append("Saved classifier: " + file.getName() + ".\n");

						} catch (Exception e1) {
							textAreaLog.append("Save failed: " + e1.toString() + "\n");
						}

					} else {
						textAreaLog.append("Save command cancelled by user.\n");
					}
					textAreaLog.setCaretPosition(textAreaLog.getDocument().getLength()); // nao sei o que faz!
				}
			}
		});

		timerHasFileReadingTime = false;
		final JCheckBox checkboxTimeReadingFile = new JCheckBox("Time including File reading");
		checkboxTimeReadingFile.addItemListener(new ItemListener() {

			public void itemStateChanged(ItemEvent arg0) {
				if (checkboxTimeReadingFile.isSelected()) {
					timerHasFileReadingTime = true;
					lblTimer.setText(String.valueOf(timerWithFileReading) + " seconds");
				} else {
					timerHasFileReadingTime = false;
					lblTimer.setText(String.valueOf(timerWithoutFileReading) + " seconds");
				}
			}
		});

		JButton btnLoadClassifier = new JButton("Load classifier");
		btnLoadClassifier.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int returnVal = fChooser.showOpenDialog(contentPane);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fChooser.getSelectedFile();

					try {
						classifier = TreeAugmentedNaiveBayesClassifier.loadClassifier(file);
						textAreaLog.append("Loaded classifier: " + file.getName() + ".\n");

					} catch (Exception e1) {
						textAreaLog.append("Load failed: " + e1.toString() + "\n");
					}

				} else {
					textAreaLog.append("Load command cancelled by user.\n");
				}
				textAreaLog.setCaretPosition(textAreaLog.getDocument().getLength());
			}
		});

		chckbxSaveOutputTo = new JCheckBox("Save output to file");

		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane
				.setHorizontalGroup(gl_contentPane
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								gl_contentPane.createSequentialGroup().addContainerGap()
										.addComponent(separator_1, GroupLayout.DEFAULT_SIZE, 499, Short.MAX_VALUE).addGap(4))
						.addGroup(
								gl_contentPane
										.createSequentialGroup()
										.addGroup(
												gl_contentPane
														.createParallelGroup(Alignment.LEADING)
														.addGroup(
																gl_contentPane.createSequentialGroup().addContainerGap()
																		.addComponent(logScrollPane, GroupLayout.DEFAULT_SIZE, 493, Short.MAX_VALUE))
														.addGroup(
																gl_contentPane
																		.createSequentialGroup()
																		.addGap(20)
																		.addGroup(
																				gl_contentPane
																						.createParallelGroup(Alignment.LEADING, false)
																						.addGroup(
																								gl_contentPane
																										.createSequentialGroup()
																										.addPreferredGap(ComponentPlacement.RELATED)
																										.addComponent(testFileButton,
																												GroupLayout.DEFAULT_SIZE,
																												GroupLayout.DEFAULT_SIZE,
																												Short.MAX_VALUE))
																						.addComponent(trainFileButton, GroupLayout.DEFAULT_SIZE,
																								GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
																		.addPreferredGap(ComponentPlacement.RELATED)
																		.addGroup(
																				gl_contentPane
																						.createParallelGroup(Alignment.LEADING)
																						.addComponent(testFileLabel, GroupLayout.DEFAULT_SIZE, 374,
																								Short.MAX_VALUE)
																						.addComponent(trainFileLabel, GroupLayout.DEFAULT_SIZE, 374,
																								Short.MAX_VALUE)))
														.addGroup(
																gl_contentPane
																		.createSequentialGroup()
																		.addContainerGap()
																		.addGroup(
																				gl_contentPane
																						.createParallelGroup(Alignment.LEADING)
																						.addGroup(
																								gl_contentPane
																										.createSequentialGroup()
																										.addGap(10)
																										.addGroup(
																												gl_contentPane
																														.createParallelGroup(
																																Alignment.LEADING,
																																false)
																														.addComponent(
																																btnSaveClassifier,
																																GroupLayout.DEFAULT_SIZE,
																																GroupLayout.DEFAULT_SIZE,
																																Short.MAX_VALUE)
																														.addComponent(
																																scoreModelComboBox,
																																0, 104,
																																Short.MAX_VALUE)
																														.addComponent(
																																btnLoadClassifier,
																																GroupLayout.DEFAULT_SIZE,
																																GroupLayout.DEFAULT_SIZE,
																																Short.MAX_VALUE))
																										.addPreferredGap(ComponentPlacement.UNRELATED)
																										.addGroup(
																												gl_contentPane
																														.createParallelGroup(
																																Alignment.TRAILING)
																														.addGroup(
																																gl_contentPane
																																		.createSequentialGroup()
																																		.addComponent(
																																				lblTimer,
																																				GroupLayout.DEFAULT_SIZE,
																																				220,
																																				Short.MAX_VALUE)
																																		.addPreferredGap(
																																				ComponentPlacement.RELATED)
																																		.addComponent(
																																				checkboxTimeReadingFile))
																														.addComponent(
																																btnConstructClassifier,
																																GroupLayout.DEFAULT_SIZE,
																																369, Short.MAX_VALUE)))
																						.addComponent(separator, GroupLayout.DEFAULT_SIZE, 493,
																								Short.MAX_VALUE)))).addContainerGap())
						.addGroup(
								Alignment.TRAILING,
								gl_contentPane
										.createSequentialGroup()
										.addGroup(
												gl_contentPane
														.createParallelGroup(Alignment.TRAILING)
														.addGroup(
																gl_contentPane
																		.createSequentialGroup()
																		.addContainerGap()
																		.addComponent(btnClassifyTestFile, GroupLayout.DEFAULT_SIZE, 372,
																				Short.MAX_VALUE).addPreferredGap(ComponentPlacement.UNRELATED)
																		.addComponent(chckbxSaveOutputTo))
														.addGroup(
																gl_contentPane.createSequentialGroup().addGap(2)
																		.addComponent(separator_2, GroupLayout.DEFAULT_SIZE, 501, Short.MAX_VALUE)))
										.addContainerGap()));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(
				gl_contentPane
						.createSequentialGroup()
						.addGap(5)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE).addComponent(trainFileButton).addComponent(trainFileLabel))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE).addComponent(testFileButton).addComponent(testFileLabel))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(separator, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGap(1)
						.addGroup(
								gl_contentPane
										.createParallelGroup(Alignment.BASELINE)
										.addComponent(scoreModelComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE).addComponent(btnConstructClassifier))
						.addGroup(
								gl_contentPane
										.createParallelGroup(Alignment.LEADING)
										.addGroup(
												gl_contentPane
														.createSequentialGroup()
														.addPreferredGap(ComponentPlacement.RELATED)
														.addGroup(
																gl_contentPane
																		.createParallelGroup(Alignment.BASELINE)
																		.addComponent(lblTimer, GroupLayout.PREFERRED_SIZE, 37,
																				GroupLayout.PREFERRED_SIZE).addComponent(checkboxTimeReadingFile)))
										.addGroup(
												gl_contentPane.createSequentialGroup().addGap(6).addComponent(btnLoadClassifier).addGap(3)
														.addComponent(btnSaveClassifier)))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(separator_2, GroupLayout.PREFERRED_SIZE, 10, GroupLayout.PREFERRED_SIZE)
						.addGap(3)
						.addGroup(
								gl_contentPane.createParallelGroup(Alignment.BASELINE).addComponent(btnClassifyTestFile)
										.addComponent(chckbxSaveOutputTo)).addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(separator_1, GroupLayout.PREFERRED_SIZE, 2, GroupLayout.PREFERRED_SIZE).addGap(4)
						.addComponent(logScrollPane, GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE).addGap(13)));
		contentPane.setLayout(gl_contentPane);
	}
}
