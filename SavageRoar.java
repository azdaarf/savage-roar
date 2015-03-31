import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

class SavageRoar extends JFrame {

	JCheckBox FoNCB1 = new JCheckBox("Force of Nature");
	JCheckBox SRCB1 = new JCheckBox("Savage Roar #1");
	JCheckBox SRCB2 = new JCheckBox("Savage Roar #2");
	ArrayList<Minion> minionList = new ArrayList<>();
	boolean FoN = false;

	JPanel center = new JPanel();
	int totalDMG = 0;
	JLabel totalDMGL = new JLabel("Total Damage: " + totalDMG);
	int SR = 0;
	int minionCount = 0;

	ActionListener aMB = new addMinionButton();
// aids2
//	aids
	SavageRoar(){

		super("Savage Roar Calculator");

		//center start
		center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
		JScrollPane scroll = new JScrollPane(center);
		add(scroll, BorderLayout.CENTER);
		//center end

		//right start
		JPanel right = new JPanel();
		right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
		add(right, BorderLayout.EAST);
		right.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

		right.add(totalDMGL);
		right.add(Box.createVerticalGlue());
		JLabel spells = new JLabel(" Spells");
		right.add(spells);
		spells.setAlignmentX(0);
		right.add(FoNCB1);
		right.add(SRCB1);
		right.add(SRCB2);
		//right end

		//bottom start
		JPanel bottom = new JPanel();
		bottom.setLayout(new BoxLayout(bottom, BoxLayout.X_AXIS));
		add(bottom, BorderLayout.SOUTH);
		bottom.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

//		ActionListener aMB = new addMinionButton();
		
		for (int i =0; i <= 8; i++){
			JButton b = new JButton(""+i);
			bottom.add(b);
//			b.addActionListener(new addMinionButton());
			b.addActionListener(aMB);
		}
		//		bottom.add(Box.createRigidArea(new Dimension(10, 0)));
		//bottom end


		FoNCB1.addActionListener(new FoNClick());
		SRCB1.addActionListener(new SRClick());
		SRCB2.addActionListener(new SRClick());

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(400,600);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public class Minion extends JPanel {
		JLabel attackLabel = new JLabel("Attack:");
		JTextField minionAttack = new JTextField(2);
		JLabel SRLabel = new JLabel("+"+SR + "");
		JButton removeButton = new JButton("X");
		boolean FoN;
		int attack;
		public Minion(int attack){
			if(attack == 999){
				attack=2;
				minionAttack.setEditable(false);
				FoN = true;
				removeButton.setEnabled(false);
			}
			add(attackLabel);
			add(minionAttack);
			add(SRLabel);
			add(removeButton);

			minionAttack.setText(""+attack);
			minionAttack.addActionListener(new attackEdit());
			removeButton.addActionListener(new removeButton());

			this.attack = attack;
		}

		public void setSR(int SR){
			SRLabel.setText("+"+SR + "");
		}
		
		public void setAttack(int attack){
			this.attack = attack;
		}

		public boolean getFoN(){
			return FoN;
		}

		public int getAttack(){
			return attack;
		}

		public JButton getRemove(){
			return removeButton;
		}
		
		public JTextField getAttackTF(){
			return minionAttack;
		}
	}

	class removeButton implements ActionListener{
		public void actionPerformed(ActionEvent aev){
			JButton b = (JButton)aev.getSource();
			int i = -1;
			for (Minion m : minionList)
				if (m.getRemove() == b)
					i = minionList.indexOf(m);
			removeMinion(i);
			updateDMG();
		}
	}

	
	
	class addMinionButton implements ActionListener{
		public void actionPerformed(ActionEvent aev){
			JButton b = (JButton)aev.getSource();
			addMinion(Integer.parseInt(b.getText()));
			updateDMG();
		}
	}

	class attackEdit implements ActionListener{
		public void actionPerformed(ActionEvent aev){
			JTextField tf = (JTextField)aev.getSource();
			for (Minion m : minionList)
				if (m.getAttackTF() == tf)
					m.setAttack(Integer.parseInt(tf.getText()));
			tf.getText();
			updateDMG();
		}
	}

	class FoNClick implements ActionListener{
		public void actionPerformed(ActionEvent aev){
			if (FoNCB1.isSelected()){
				for (int j = 1; j <=3; j++)
					addMinion(999);
			}
			else
				removeFoN();
			updateDMG();
		}
	}

	class SRClick implements ActionListener{
		public void actionPerformed(ActionEvent aev){
			if (SRCB1.isSelected() && !SRCB2.isSelected())
				SR = 2;
			else if (SRCB2.isSelected() && !SRCB1.isSelected())
				SR = 2;
			else if (SRCB1.isSelected() && SRCB2.isSelected())
				SR = 4;
			else 
				SR = 0;
			for (Minion m : minionList)
				m.setSR(SR);
			updateDMG();
		}
	}

	
	/**
	 * Removes a minion from the frame.
	 * 
	 * @param the id of the minion to be removed.
	 */
	private void removeMinion (int number){
		minionList.remove(number);
		center.remove(number);
		minionCount--;
		center.revalidate();
		center.repaint();
	}

	void removeFoN(){
		int[] FoNnr = {-1, -1, -1};
		int FoNcount = 0;
		for (int i = 0; i < minionList.size(); i++)
			if (minionList.get(i).getFoN()){
				FoNnr[FoNcount++] = i;
			}
		for (int i = 0; i < 3; i++)
			if (FoNnr[0]!= -1)
				removeMinion(FoNnr[0]);
	}

	void updateDMG(){
		totalDMG = 0;
		for (Minion m : minionList)
			totalDMG += m.getAttack();
		totalDMG += SR*(minionCount+1);
		totalDMGL.setText("Total Damage: " + totalDMG);
		System.out.println(""+totalDMG);
	}

	void addMinion(int attack){
		if (minionCount < 7){
			Minion m = new Minion(attack);
			minionList.add(m);
			center.add(minionList.get(minionCount));
			minionCount++;
			center.revalidate();
		}
	}

	Minion getMinion(int number){
		Minion m = minionList.get(number);
		return m;
	}

	public static void main (String[] args){

		new SavageRoar();

	}

}
