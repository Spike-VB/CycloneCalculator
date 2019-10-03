import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.EtchedBorder;
import java.io.*;


public class CycloneCalculator {
    
    double Q;
    CycloneCalculator cycloneCalculator;
    JFrame frame = new JFrame("Cyclone calculator");
    //JFrame frame = new JFrame("Программа расчета количества циклонов и внутреннего диаметра фильтра-сепаратора.");
    JPanel dataPanel;
    JPanel sourceDataPanel;
    JPanel resultDataPanel;
    JCheckBox checkBox1;
    JCheckBox checkBox2;
    
    JTextField value1 = new JTextField();
    JTextField value2 = new JTextField();
    JTextField value3 = new JTextField();
    JTextField value4 = new JTextField(10);
    JTextField value5 = new JTextField("2.8", 10);
    JTextField value6 = new JTextField("77", 10);
    JTextField value7 = new JTextField("80", 10);
    JTextField value8 = new JTextField("60", 10);
    Label value9 = new Label("", Label.CENTER);
    Label value10 = new Label("", Label.CENTER);
    Label value11 = new Label("", Label.CENTER);
    
    //String[] units1 = {"m^3/day*10^6", "m^3/h*10^3", "m^3/min*10^3", "m^3/s"};
    String[] units1 = {"млн.м3/сут", "тыс.м3/ч", "тыс.м3/мин", "м3/с"};
	JComboBox<String> unit1 = new JComboBox<String>(units1);
    //String[] units4 = {"m^3/s", "m^3/min*10^3", "m^3/h*10^3", "m^3/day*10^6"};
    String[] units4 = {"м3/с", "тыс.м3/мин", "тыс.м3/ч", "млн.м3/сут"};
	JComboBox<String> unit4 = new JComboBox<String>(units4);
    
    Label warningLabel = new Label("", Label.CENTER);
    
    int nHor;
    ArrayList<Integer> nVert1;
    ArrayList<Integer> nVert2;
    
    MyDrawPanel drawPanel = new MyDrawPanel();

    public static void main(String[] args) {
        CycloneCalculator app = new CycloneCalculator();
        app.appStart(app);
    }
    
    public void appStart(CycloneCalculator app) {
        cycloneCalculator = app;
        cycloneCalculator.buildGUI();
    }

    public void buildGUI() {
        value1.addCaretListener(new value1Listener());
        value2.addCaretListener(new value1Listener());
        value3.addCaretListener(new value1Listener());
        value4.addCaretListener(new value1Listener());
        value5.addCaretListener(new value1Listener());
        value6.addCaretListener(new value1Listener());
        value7.addCaretListener(new value1Listener());
        value8.addCaretListener(new value1Listener());
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(200,70,1380,840);
        frame.setVisible(true);
        
        JMenuBar menuBar = new JMenuBar();
        
        JMenu fileMenu = new JMenu("File");
        fileMenu.setFont(new Font(Font.DIALOG, Font.PLAIN, 16));
        
        JMenuItem saveMenuItem = new JMenuItem("Save file as .txt");
        saveMenuItem.setFont(new Font(Font.DIALOG, Font.PLAIN, 16));
        saveMenuItem.addActionListener(new saveMenuListener());
        
        JMenuItem openMenuItem = new JMenuItem("Open file");
        openMenuItem.setFont(new Font(Font.DIALOG, Font.PLAIN, 16));
        openMenuItem.addActionListener(new openMenuListener());
        
        fileMenu.add(openMenuItem);
        fileMenu.add(saveMenuItem);
        menuBar.add(fileMenu);
        frame.setJMenuBar(menuBar);

        //Label mainLabel = new Label("Cyclone calculating program", Label.CENTER);
        Label mainLabel = new Label("Программа расчёта количества циклонов и "
                + "внутреннего диаметра фильтр-сепаратора.", Label.CENTER);
        mainLabel.setFont(new Font(Font.DIALOG, Font.PLAIN, 18));
        frame.getContentPane().add(BorderLayout.NORTH, mainLabel);

        JPanel runButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        runButtonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.getContentPane().add(BorderLayout.SOUTH, runButtonPanel);

        //JButton runButton = new JButton("Calculate");
        JButton runButton = new JButton("Рассчитать");
        runButton.setFont(new Font(Font.DIALOG, Font.PLAIN, 18));
        runButtonPanel.add(runButton);
        runButton.addActionListener(new runButtonListener());

        dataPanel = new JPanel();
        dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.Y_AXIS));
        dataPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 4));
        frame.getContentPane().add(BorderLayout.WEST, dataPanel);

        JPanel checkBoxPanel = new JPanel();
        checkBoxPanel.setLayout(new BoxLayout(checkBoxPanel, BoxLayout.Y_AXIS));
        checkBoxPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        dataPanel.add(checkBoxPanel);

        JPanel checkBox1Panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        checkBoxPanel.add(checkBox1Panel);

        checkBox1 = new JCheckBox();
        checkBox1.addActionListener(new CheckBox1Listener());
        checkBox1.setSelected(true);
        checkBox1Panel.add(checkBox1);

        //Label checkBox1Label = new Label("calculaion by gas volume flow, reduced to standard conditions");
        Label checkBox1Label = new Label("расчёт по объёмному расходу газа,"
                + "приведённому к стандартным условиям по ГОСТ 2939-63");
        checkBox1Panel.add(checkBox1Label);

        JPanel checkBox2Panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        checkBoxPanel.add(checkBox2Panel);

        checkBox2 = new JCheckBox();
        checkBox2.addActionListener(new CheckBox2Listener());
        checkBox2.setSelected(false);
        checkBox2Panel.add(checkBox2);

        //Label checkBox2Label = new Label("calculation by gas volume flow at working conditions");
        Label checkBox2Label = new Label("расчёт по объёмному расходу газа в рабочих условиях");
        checkBox2Panel.add(checkBox2Label);
        
        sourceDataPanel = new JPanel();
        sourceDataPanel.setLayout(new BoxLayout(sourceDataPanel, BoxLayout.X_AXIS));
        //sourceDataPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder
        //(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),"Source data"), BorderFactory.createEmptyBorder(30, 30, 30, 30)));
        //dataPanel.add(sourceDataPanel);
        sourceDataPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder
        (BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),"Исходные данные"), BorderFactory.createEmptyBorder(30, 30, 30, 30)));
        dataPanel.add(sourceDataPanel);
        
        cycloneCalculator.addSourceDataPanelContent();

        resultDataPanel = new JPanel();
        resultDataPanel.setLayout(new BoxLayout(resultDataPanel, BoxLayout.X_AXIS));
        //resultDataPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder
        //(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),"Result data"), BorderFactory.createEmptyBorder(30, 30, 30, 30)));
        //dataPanel.add(resultDataPanel);
        resultDataPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder
        (BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),"Результаты расчёта"), BorderFactory.createEmptyBorder(30, 30, 30, 30)));
        dataPanel.add(resultDataPanel);

        JPanel resultNamePanel = new JPanel();
        resultNamePanel.setLayout(new BoxLayout(resultNamePanel, BoxLayout.Y_AXIS));
        resultDataPanel.add(resultNamePanel);

        //Label name9 = new Label("Number of cyclones -", Label.RIGHT);
        Label name9 = new Label("Количество циклонов -", Label.RIGHT);
        //Label name10 = new Label("Filter-separator internal diameter -", Label.RIGHT);
        Label name10 = new Label("Внутренний диаметр фильтр-сепаратора -", Label.RIGHT);
        //Label name11 = new Label("Filter-separator maximum number of cyclones -", Label.RIGHT);
        Label name11 = new Label("Максимальное количество циклонов, вписываемое", Label.RIGHT);
        Label name11_1 = new Label("во внутренний диаметр фильтр-сепаратора -", Label.RIGHT);
        resultNamePanel.add(name9);
        resultNamePanel.add(name10);
        resultNamePanel.add(name11);
        resultNamePanel.add(name11_1);

        JPanel resultValuePanel = new JPanel();
        resultValuePanel.setLayout(new BoxLayout(resultValuePanel, BoxLayout.Y_AXIS));
        resultDataPanel.add(resultValuePanel);
        
        value1.setHorizontalAlignment(JTextField.CENTER);
        value2.setHorizontalAlignment(JTextField.CENTER);
        value3.setHorizontalAlignment(JTextField.CENTER);
        value4.setHorizontalAlignment(JTextField.CENTER);
        value5.setHorizontalAlignment(JTextField.CENTER);
        value6.setHorizontalAlignment(JTextField.CENTER);
        value7.setHorizontalAlignment(JTextField.CENTER);
        value8.setHorizontalAlignment(JTextField.CENTER);

        resultValuePanel.add(value9);
        resultValuePanel.add(value10);
        resultValuePanel.add(new Label());
        resultValuePanel.add(value11);

        JPanel resultUnitPanel = new JPanel();
        resultUnitPanel.setLayout(new BoxLayout(resultUnitPanel, BoxLayout.Y_AXIS));
        resultDataPanel.add(resultUnitPanel);

        //Label unit9 = new Label("pcs");
        Label unit9 = new Label("шт");
        //Label unit10 = new Label("mm");
        Label unit10 = new Label("мм");
        //Label unit11 = new Label("pcs");
        Label unit11 = new Label("шт");
        resultUnitPanel.add(unit9);
        resultUnitPanel.add(unit10);
        resultUnitPanel.add(new Label());
        resultUnitPanel.add(unit11);
        
        dataPanel.add(warningLabel);
        
        JPanel backPlotPanel = new JPanel();
        backPlotPanel.setLayout(new BoxLayout(backPlotPanel, BoxLayout.Y_AXIS));
        backPlotPanel.setBorder(BorderFactory.createEmptyBorder(10, 4, 10, 10));
        
        JPanel plotPanel = new JPanel();
        plotPanel.setLayout(new BoxLayout(plotPanel, BoxLayout.Y_AXIS));
        plotPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder
        (BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),"Схема расположения цикловнов в корпусе "
                + "фильтр-сепаратора"), BorderFactory.createEmptyBorder(30, 30, 30, 30)));
        frame.getContentPane().add(BorderLayout.CENTER, backPlotPanel);
        backPlotPanel.add(plotPanel);
        plotPanel.add(drawPanel);

        frame.revalidate();
    }
    
    public void addSourceDataPanelContent() {
        JPanel sourceNamePanel = new JPanel();
        sourceNamePanel.setLayout(new BoxLayout(sourceNamePanel, BoxLayout.Y_AXIS));
        sourceDataPanel.add(sourceNamePanel);

        JPanel sourceValuePanel = new JPanel();
        sourceValuePanel.setLayout(new BoxLayout(sourceValuePanel, BoxLayout.Y_AXIS));
        sourceDataPanel.add(sourceValuePanel);

        JPanel sourceUnitPanel = new JPanel();
        sourceUnitPanel.setLayout(new BoxLayout(sourceUnitPanel, BoxLayout.Y_AXIS));
        sourceDataPanel.add(sourceUnitPanel);
        
        if(checkBox1.isSelected()) {
            //Label name1 = new Label("Gas volume flow, reduced to standard conditions -", Label.RIGHT);
            Label name1 = new Label("Объёмный расход газа, приведенный к стандартным условиям -", Label.RIGHT);
            //Label name2 = new Label("Gas pressure at working conditions -", Label.RIGHT);
            Label name2 = new Label("Давление газа в рабочих условиях -", Label.RIGHT);
            //Label name3 = new Label("Gas temperature at working conditions -", Label.RIGHT);
            Label name3 = new Label("Температура газа в рабочих условиях -", Label.RIGHT);
            sourceNamePanel.add(name1);
            sourceNamePanel.add(name2);
            sourceNamePanel.add(name3);
	
            sourceValuePanel.add(value1);
            sourceValuePanel.add(value2);
            sourceValuePanel.add(value3);

            unit1.setFont(new Font(Font.DIALOG, Font.PLAIN, 12));
            unit1.addActionListener(new unit1Listener());
            
            //Label unit2 = new Label("MPa");
            Label unit2 = new Label("МПа");
            Label unit3 = new Label("C");
            sourceUnitPanel.add(unit1);
            sourceUnitPanel.add(unit2);
            sourceUnitPanel.add(unit3);
           }

        //Label name4 = new Label("Gas volume flow at working conditions -", Label.RIGHT);
        Label name4 = new Label("Объёмный расход газа в рабочих условиях -", Label.RIGHT);
        //Label name5 = new Label("Cyclone gas velocity (2.5 - 3 m3/s) -", Label.RIGHT);
        Label name5 = new Label("Скорость потока газа в циклоне (2.5 - 3 м3/с) -", Label.RIGHT);
        //Label name6 = new Label("Cyclone internal diameter -", Label.RIGHT);
        Label name6 = new Label("Внутренний диаметр циклона -", Label.RIGHT);
        //Label name7 = new Label("Grid step -", Label.RIGHT);
        Label name7 = new Label("Шаг сетки -", Label.RIGHT);
        //Label name8 = new Label("Grid angle -", Label.RIGHT);
        Label name8 = new Label("Угол сетки -", Label.RIGHT);
        sourceNamePanel.add(name4);
        sourceNamePanel.add(name5);
        sourceNamePanel.add(name6);
        sourceNamePanel.add(name7);
        sourceNamePanel.add(name8); 
        
        sourceValuePanel.add(value4);
        sourceValuePanel.add(value5);
        sourceValuePanel.add(value6);
        sourceValuePanel.add(value7);
        sourceValuePanel.add(value8);

        unit4.setFont(new Font(Font.DIALOG, Font.PLAIN, 12));
        unit4.addActionListener(new unit4Listener());
        
        //Label unit5 = new Label("m/s");
        Label unit5 = new Label("м/с");
        //Label unit6 = new Label("mm");
        Label unit6 = new Label("мм");
        //Label unit7 = new Label("mm");
        Label unit7 = new Label("мм");
        //Label unit8 = new Label("deg");
        Label unit8 = new Label("град");
        sourceUnitPanel.add(unit4);
        sourceUnitPanel.add(unit5);
        sourceUnitPanel.add(unit6);
        sourceUnitPanel.add(unit7);
        sourceUnitPanel.add(unit8);
    }

    private class MyDrawPanel extends JPanel {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void paintComponent(Graphics g) {
            g.setColor(this.getBackground());
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
            
            g.setColor(Color.white);
            
            int panelSize = this.getHeight();
            
            if (this.getWidth() < this.getHeight()) {
                panelSize = this.getWidth();
            }
            
            g.fillRect((this.getWidth() - panelSize) / 2, 0, panelSize, panelSize);
            
            g.setColor(Color.black);
            g.setFont(g.getFont().deriveFont((float) 16));
            
            if(nHor > 0) {
                
                int stepHor = (panelSize - 100) / (nHor - 1);
                int stepVert = (panelSize - 130) / (nVert1.get(0) - 1);

                if(nVert2.get(nVert2.size() - 1) != 0)  {
                    stepHor = (panelSize - 100 - stepHor / 2) / (nHor - 1);
                }
                
                if(nVert1.get(0) == nVert2.get(0)) {
                    stepVert = (panelSize - 130 - stepVert / 2) / (nVert1.get(0) - 1);
                }
                
                g.drawString("" + nHor, 27 + (this.getWidth() - panelSize) / 2, panelSize - 77);
            
                for(int tHor = 0; tHor < nHor; tHor++) {
                    g.setColor(Color.black);
                    for(int tVert1 = 0; tVert1 < nVert1.get(tHor); tVert1++) {
                        g.fillOval(50 + (this.getWidth() - panelSize) / 2 + tHor*stepHor, panelSize - 90 - tVert1*stepVert, 10, 10);
                    }
                    g.drawString("" + nVert1.get(tHor), 53 + (this.getWidth() - panelSize) / 2 + tHor*stepHor, panelSize - 60);
                    g.setColor(Color.gray);
                    for(int tVert2 = 0; tVert2 < nVert2.get(tHor); tVert2++) {
                        g.fillOval(50 + stepHor / 2 + (this.getWidth() - panelSize) / 2 + tHor*stepHor, panelSize - 90 - stepVert / 2 - tVert2*stepVert, 10, 10);
                    }
                    if(nVert2.get(tHor) != 0) {
                        g.drawString("" + nVert2.get(tHor), 53 + (this.getWidth() - panelSize) / 2 + stepHor / 2 + tHor*stepHor, panelSize - 40);
                    }
                }
            }
        }
    }

    public class runButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            double Qst;
            double P;
            double T;
            if(checkBox1.isSelected()) {
                Qst = Double.parseDouble(value1.getText());
                switch(unit1.getSelectedIndex()) {
                    case 0 :
                        Qst = Qst * Math.pow(10, 6) / (3600 * 24);
                        break;
                    case 1 :
                        Qst = Qst * Math.pow(10, 3) / 3600;
                        break;
                    case 2 :
                        Qst = Qst * Math.pow(10, 3) / 60;
                        break;
                }
                P = Double.parseDouble(value2.getText()) * Math.pow(10, 6);
                T = Double.parseDouble(value3.getText()) + 273.15;
                Q = 101325 * Qst * T / (P * 293.15);
                
                switch(unit4.getSelectedIndex()) {
                    case 0 :
                        value4.setText("" + Math.rint(Q * 100) / 100);
                        break;
                    case 1 :
                        value4.setText("" + Math.rint((Q*60 / Math.pow(10, 3)) * 100) / 100);
                        break;
                    case 2 :
                        value4.setText("" + Math.rint((Q*3600 / Math.pow(10, 3)) * 100) / 100);
                        break;
                    case 3 :
                        value4.setText("" + Math.rint((Q*3600*24 / Math.pow(10, 6)) * 100) / 100);
                        break;
                }
            } else {
                Q = Double.parseDouble(value4.getText());
                switch(unit4.getSelectedIndex()) {
                    case 1 :
                        Q = Q * Math.pow(10, 3) / 60;
                        break;
                    case 2 :
                        Q = Q * Math.pow(10, 3) / 3600;
                        break;
                    case 3 :
                        Q = Q * Math.pow(10, 6) / (3600 * 24);
                        break;
                }
            }

            double v = Double.parseDouble(value5.getText());
            double d = Double.parseDouble(value6.getText()) / Math.pow(10, 3);
            double b = Double.parseDouble(value7.getText()) / Math.pow(10, 3);
            double alf = Double.parseDouble(value8.getText()) / 57.2958;
            
            double[] Dst = {250, 300, 350, 400, 500, 600, 700, 800, 900, 1000,
                1200, 1400, 1600, 1800, 2000, 2200, 2400, 2500, 2600, 2800,
                3000, 3200, 3400, 3600, 3800, 4000, 4500};
            
            double n = Math.round(4*Q / (3.14*v*Math.pow(d, 2)));
            double D = Math.sqrt(2*n*Math.pow(b, 2) / (3.14*Math.tan(alf / 2)));
            
            double Dr;
            double N = 0;
            
            while(N < n) {
                
                nVert1 = new ArrayList<Integer>();
                nVert2 = new ArrayList<Integer>();
                
                if(D > Dst[Dst.length - 1] / 1000) {
                    warningLabel.setText("Элиптические днища требуемого диаметра "
                            + "отсутствуют в ГОСТ 6533-78.");
                } else {
                    warningLabel.setText("");
                    for(int i = 0; i < Dst.length; i++) {
                        if(D > Dst[i] / 1000 && D <= Dst[i + 1] / 1000) {
                            D = Dst[i + 1] / 1000;
                        } else if(D <= Dst[0] / 1000) {
                            D = Dst[0] / 1000;
                        }
                    }
                }

                Dr = D - (double) 40 / 1000; //учитываем зазор между крайними циклонами и стенкой корпуса
                
                nHor =(int) Math.floor(Dr / (2*b));
                
                while(nHor*b + d / 2 > Dr / 2) {
                    nHor = nHor - 1;
                }
                
                nHor++;

                for(int i = 0; i < nHor; i++) {
                    nVert1.add(i, 1);
                    while(Math.sqrt(Math.pow(i*b, 2) + Math.pow(b*nVert1.get(i) / Math.tan(alf / 2), 2)) + d / 2 <= Dr / 2) {
                        nVert1.set(i, nVert1.get(i) + 1);
                    }
                    nVert2.add(i, 0);
                    while(Math.sqrt(Math.pow(i*b + b/2, 2) + Math.pow(b*(2*nVert2.get(i) + 1) / (2*Math.tan(alf / 2)), 2)) + d / 2 <= Dr / 2) {
                        nVert2.set(i, nVert2.get(i) + 1);
                    }
                }
                
                int[] nVertSum1 = new int[nVert1.size()];
                int[] nVertSum2 = new int[nVert2.size()];
                
                for(int i = 0; i < nVert1.size(); i++) {
                    nVertSum1[i] = nVert1.get(i);
                }
                
                for(int i = 0; i < nVert2.size(); i++) {
                    nVertSum2[i] = nVert2.get(i);
                }
                
                N = 4*(Arrays.stream(nVertSum1).sum() + Arrays.stream(nVertSum2).sum()) - 2*(nHor + nVert1.get(0)) + 1;
                
                if(N < n) {
                    D = Dr + (double) 40 / 1000 + (double) 20 / 1000;
                }               
            }
            value9.setText("" + Math.round(n));
            value10.setText("" + Math.round(D*1000));
            value11.setText("" + Math.round(N));
            drawPanel.repaint();
            frame.revalidate();
        }
    }

    public class CheckBox1Listener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if(!checkBox1.isSelected()) {
                checkBox2.setSelected(true);
                value1.setText("");
                value2.setText("");
                value3.setText("");
                unit1.setSelectedIndex(0);
            } else {
                checkBox2.setSelected(false);
            }
            sourceDataPanel.removeAll();
            cycloneCalculator.addSourceDataPanelContent();
            frame.revalidate();
        }
    }

    public class CheckBox2Listener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if(!checkBox2.isSelected()) {
                checkBox1.setSelected(true);
            } else {
                checkBox1.setSelected(false);
                value1.setText("");
                value2.setText("");
                value3.setText("");
                unit1.setSelectedIndex(0);
            }
            sourceDataPanel.removeAll();
            cycloneCalculator.addSourceDataPanelContent();
            frame.revalidate();
        }
    }
    
    public class unit1Listener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            frame.revalidate();
        }
    }
    
    public class unit4Listener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if(checkBox1.isSelected()) {
                switch(unit4.getSelectedIndex()) {
                    case 0 :
                        value4.setText("" + Math.rint(Q * 100) / 100);
                        break;
                    case 1 :
                        value4.setText("" + Math.rint((Q*60 / Math.pow(10, 3)) * 100) / 100);
                        break;
                    case 2 :
                        value4.setText("" + Math.rint((Q*3600 / Math.pow(10, 3)) * 100) / 100);
                        break;
                    case 3 :
                        value4.setText("" + Math.rint((Q*3600*24 / Math.pow(10, 6)) * 100) / 100);
                        break;
                }
            frame.revalidate();
            }
        }
    }
    
    public class openMenuListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JFileChooser openChooser = new JFileChooser();
            openChooser.showOpenDialog(frame);
            
            try {
                BufferedReader reader = new BufferedReader(new FileReader(openChooser.getSelectedFile()));
                reader.readLine();
                String line = reader.readLine();
                checkBox1.setSelected(Boolean.parseBoolean(line));
                checkBox2.setSelected(!Boolean.parseBoolean(line));
                if(checkBox2.isSelected()) {
                    value1.setText("");
                    value2.setText("");
                    value3.setText("");
                    unit1.setSelectedIndex(0);
                }
                sourceDataPanel.removeAll();
                cycloneCalculator.addSourceDataPanelContent();
                unit1.setSelectedIndex(Integer.parseInt(reader.readLine()));
                unit4.setSelectedIndex(Integer.parseInt(reader.readLine()));
                reader.readLine();
                reader.readLine();
                if(checkBox1.isSelected()) {
                    value1.setText(reader.readLine().split(" / ")[0]);
                    value2.setText(reader.readLine().split(" / ")[0]);
                    value3.setText(reader.readLine().split(" / ")[0]);
                }
                value4.setText(reader.readLine().split(" / ")[0]);
                value5.setText(reader.readLine().split(" / ")[0]);
                value6.setText(reader.readLine().split(" / ")[0]);
                value7.setText(reader.readLine().split(" / ")[0]);
                value8.setText(reader.readLine().split(" / ")[0]);
                reader.close();
                
                value9.setText("");
                value10.setText("");
                value11.setText("");
                warningLabel.setText("");
                nHor = 0;
                drawPanel.repaint();
                
            } catch(IOException ex) {
                System.out.println("Couldn't open the file");
                ex.printStackTrace();
            }
            frame.revalidate();
        }
    }
    
    public class saveMenuListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JFileChooser saveChooser = new JFileChooser();
            saveChooser.showSaveDialog(frame);
            
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(saveChooser.getSelectedFile()));
                writer.write("Вспомогательные данные:" + "\n");
                writer.write("" + checkBox1.isSelected() + "\n");
                writer.write(unit1.getSelectedIndex() + "\n");
                writer.write(unit4.getSelectedIndex() + "\n");
                writer.write("\n");
                writer.write("Исходные данные:" + "\n");
                if (checkBox1.isSelected()) {
                    writer.write(value1.getText() + " / " + unit1.getItemAt(unit1.getSelectedIndex()) + 
                            " - Объёмный расход газа, приведенный к стандартным условиям" + "\n");
                    writer.write(value2.getText() + " / " + "МПа - Давление газа в рабочих условиях" + "\n");
                    writer.write(value3.getText() + " / " + "C - Температура газа в рабочих условиях" + "\n");
                }
                writer.write(value4.getText() + " / " + unit4.getItemAt(unit4.getSelectedIndex()) + 
                        " - Объёмный расход газа в рабочих условиях" + "\n");
                writer.write(value5.getText() + " / " + "м/с - Скорость потока газа в циклоне" + "\n");
                writer.write(value6.getText() + " / " + "мм - Внутренний диаметр циклона" + "\n");
                writer.write(value7.getText() + " / " + "мм - Шаг сетки" + "\n");
                writer.write(value8.getText() + " / " + "град - Угол сетки" + "\n");
                writer.write("\n");
                writer.write("Результаты расчёта:" + "\n");
                writer.write(value9.getText() + " / " + "шт - Количество циклонов" + "\n");
                writer.write(value10.getText() + " / " + "мм - Внутренний диаметр фильтр-сепаратора" + "\n");
                writer.write(value11.getText() + " / " + 
                        "шт - Максимальное количество циклонов, вписываемое во внутренний диаметр фильтр-сепаратора");
                writer.close();
            } catch(IOException ex) {
                System.out.println("Couldn't save the file");
                ex.printStackTrace();
            }
            frame.revalidate();
        }
    }
    
    public class value1Listener implements CaretListener {
        public void caretUpdate(CaretEvent e) {
            value9.setText("");
            value10.setText("");
            value11.setText("");
            warningLabel.setText("");
            nHor = 0;
            drawPanel.repaint();
            frame.revalidate();
        }
    }
    public class value2Listener implements CaretListener {
        public void caretUpdate(CaretEvent e) {
            value9.setText("");
            value10.setText("");
            value11.setText("");
            warningLabel.setText("");
            nHor = 0;
            drawPanel.repaint();
            frame.revalidate();
        }
    }
    public class value3Listener implements CaretListener {
        public void caretUpdate(CaretEvent e) {
            value9.setText("");
            value10.setText("");
            value11.setText("");
            warningLabel.setText("");
            nHor = 0;
            drawPanel.repaint();
            frame.revalidate();
        }
    }
    public class value4Listener implements CaretListener {
        public void caretUpdate(CaretEvent e) {
            value9.setText("");
            value10.setText("");
            value11.setText("");
            warningLabel.setText("");
            nHor = 0;
            drawPanel.repaint();
            frame.revalidate();
        }
    }
    public class value5Listener implements CaretListener {
        public void caretUpdate(CaretEvent e) {
            value9.setText("");
            value10.setText("");
            value11.setText("");
            warningLabel.setText("");
            nHor = 0;
            drawPanel.repaint();
            frame.revalidate();
        }
    }
    public class value6Listener implements CaretListener {
        public void caretUpdate(CaretEvent e) {
            value9.setText("");
            value10.setText("");
            value11.setText("");
            warningLabel.setText("");
            nHor = 0;
            drawPanel.repaint();
            frame.revalidate();
        }
    }
    public class value7Listener implements CaretListener {
        public void caretUpdate(CaretEvent e) {
            value9.setText("");
            value10.setText("");
            value11.setText("");
            warningLabel.setText("");
            nHor = 0;
            drawPanel.repaint();
            frame.revalidate();
        }
    }
    public class value8Listener implements CaretListener {
        public void caretUpdate(CaretEvent e) {
            value9.setText("");
            value10.setText("");
            value11.setText("");
            warningLabel.setText("");
            nHor = 0;
            drawPanel.repaint();
            frame.revalidate();
        }
    }
}