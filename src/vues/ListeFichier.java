package vues;

import java.awt.Component;
import java.awt.Rectangle;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

public class ListeFichier extends ListView<String> {

	private ListView<String> listview;
	private ObservableList<String> list; 
	private Vector<Vector<String>> vec;
	private Vector<String> columnNames;
	private DefaultTableModel model;
	private JTable table;
	private ArrayList<File> files;
	private ArrayList<String> filtres;

	public ListeFichier(ArrayList<File> files) {
		super();
		
		list = FXCollections.observableArrayList();
//		this.files = new ArrayList<File>();
//		this.files.addAll(files);
//		init();
//
//		if (table.getRowCount() > 0) {
//			remove();
//		}

//		String[] filtre = {"htm"};
		for (File file : files) {
			if (file.getAbsolutePath().endsWith(".htm")) {
				list.add(file.getAbsolutePath());
			}
		}
		this.getItems().addAll(list);
//		this.filtres = new ArrayList<String>();
//		for (String string : filtre) {
//			filtres.add(string);
//		}
//		this.updateList();
	}

//	private void init(){
//		columnNames = new Vector<String>();
//		columnNames.add(("Fichiers"));
//		columnNames.add("Type");
//
//		vec = new Vector<Vector<String>>();
//		model = new DefaultTableModel(vec, columnNames);
//		table = new JTable(model);
//
//		
//		table.getTableHeader().setReorderingAllowed(false);
//		
//		
//		this.setViewportView(table);
//	}
//
//	private void remove(){
//		vec = new Vector<Vector<String>>();
//		model.setRowCount(0);
//		table.setModel(model);
//		this.setViewportView(table);
//	}
//
//	private void updateList(){
//		Vector<String> v;
//		String fichier, type;
//
//		for (int i = 0; i < files.size(); i++) {
//			//Evite les dossiers
//			if (!files.get(i).isDirectory()) {
//				//Evite les fichiers cachés
//				String[] tab = files.get(i).getAbsolutePath().split("\\.");
//				if (filtres.contains(tab[tab.length-1])) {
//					fichier = files.get(i).getAbsolutePath();
//					v = new Vector<String>();
//					if (fichier.endsWith(".htm"))
//						type = "Page HTML";
//					else if (fichier.endsWith(".css")) 
//						type = "Feuille de style";
//					else if (fichier.endsWith(".htt"))
//						type = "Page principale";
//					else 
//						type = "autre";
//
//
//					v.addElement(fichier);
//					v.addElement(type);
//					vec.addElement(v);
//				}
//			}
//		}
//		model.setDataVector(vec, columnNames);
//		table.setModel(model);
//
//		//		// tri
//		//		if (search.getSortBy() >= 0) {
//		//			FiltreSortModel filtre;
//		//			if (search.getSortBy() == 7) 
//		//				filtre = new FiltreSortModel(model, -1);
//		//			else
//		//				filtre = new FiltreSortModel(model, 1);
//		//			table = new JTable(filtre);
//		//			filtre.sort(search.getSortBy());
//		//		}
//
//
////		table.getColumnModel().addColumnModelListener( new WrapColListener( table ) );
////		table.setDefaultRenderer( Object.class, new JTPRenderer() );
//		//table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
//
//		//Choix de la taille des colonnes
//		TableColumnModel cs = table.getColumnModel();
//		TableColumn c1 = (TableColumn) cs.getColumn(1);
//		((TableColumn) c1).setMinWidth(120);
//		((TableColumn) c1).setMaxWidth(120);
//
//		this.setViewportView(table);
//	}

//	@Override
//	public void update(Observable o, Object arg) {
//		updateList();
//	}

	class JTPRenderer extends JTextPane implements TableCellRenderer {
		private static final long serialVersionUID = 1L;

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			setText(value.toString());
			return this;
		}
	}

	class WrapColListener implements TableColumnModelListener {
		JTable m_table;
		WrapColListener( JTable table ){
			m_table = table;
		}

		void refresh_row_heights() {
			int n_rows = m_table.getRowCount();
			int n_cols = m_table.getColumnCount();
			int intercell_width = m_table.getIntercellSpacing().width;
			int intercell_height = m_table.getIntercellSpacing().height;
			TableColumnModel col_model = m_table.getColumnModel();
			if( col_model == null ) return;
			for (int row = 0; row < n_rows; row++) {
				int pref_row_height = 1;
				for (int col = 0; col < n_cols; col++) {
					Object value = m_table.getValueAt(row, col);
					TableCellRenderer renderer = m_table.getCellRenderer(row, col);
					if( renderer == null ) return;
					Component comp = renderer.getTableCellRendererComponent( m_table, value, false, false,
							row, col);
					if( comp == null ) return;
					int col_width = col_model.getColumn(col).getWidth();
					comp.setBounds(new Rectangle(0, 0, col_width - intercell_width, Integer.MAX_VALUE )); int pref_cell_height = comp.getPreferredSize().height  + intercell_height;
					if (pref_cell_height > pref_row_height) {
						pref_row_height = pref_cell_height;
					}
				}
				if (pref_row_height != m_table.getRowHeight(row)) {
					m_table.setRowHeight(row, pref_row_height);
				}
			}
		}

		@Override
		public void columnAdded(TableColumnModelEvent e) {
			refresh_row_heights();

		}

		@Override
		public void columnRemoved(TableColumnModelEvent e) {
		}

		@Override
		public void columnMoved(TableColumnModelEvent e) {
		}

		@Override
		public void columnMarginChanged(ChangeEvent e) {
			refresh_row_heights();
		}

		@Override
		public void columnSelectionChanged(ListSelectionEvent e) {
		}
	}

	@SuppressWarnings("serial")
	class FiltreSortModel extends AbstractTableModel{
		TableModel model;
		Line [] lines;
		int columnSort, order;
		FiltreSortModel (TableModel m, int order){
			model = m;
			this.order = order;
			lines = new Line[model.getRowCount()];
			for( int i = 0; i < lines.length; ++i)
				lines[i] = new Line(i);
		}
		public int getRowCount() {
			return model.getRowCount();
		}
		public int getColumnCount() {
			return model.getColumnCount();
		}
		public Object getValueAt(int rowIndex, int columnIndex) {
			return model.getValueAt(lines[rowIndex].index,  columnIndex);
		}
		public Class<?> getColumnClass( int i){
			return model.getColumnClass(i);
		}
		public String getColumnName(int i){
			return model.getColumnName(i);
		}

		public void sort(int c){
			columnSort = c; 
			try{
				Arrays.sort(lines);
				fireTableDataChanged();
			}catch (RuntimeException e){e.printStackTrace();}  // The data are not comparable !
		}
		//------- The class Line -------
		@SuppressWarnings("rawtypes")
		private class Line implements Comparable{
			int index;
			public Line (int i){index = i;}
			public int compareTo(Object o) {
				Line otherLine = (Line)o;
				Object cell = model.getValueAt(index, columnSort);
				Object otherCell = model.getValueAt(otherLine.index, columnSort);
				if (order > 0) 
					return ((String) cell).compareTo((String) otherCell);
				else
					return -((String) cell).compareTo((String) otherCell);

			}
		}
	}

	public JTable getTable() {
		return table;
	}

	public void setTable(JTable table) {
		this.table = table;
	}

}
