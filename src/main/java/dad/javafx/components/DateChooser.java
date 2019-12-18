package dad.javafx.components;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.ResourceBundle;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;

public class DateChooser extends HBox implements Initializable {

	// model

	private ObjectProperty<LocalDate> dateProperty = new SimpleObjectProperty<LocalDate>();
	private IntegerProperty mes = new SimpleIntegerProperty();
	private IntegerProperty dia = new SimpleIntegerProperty();
	private IntegerProperty anoConvertido = new SimpleIntegerProperty();
	private StringProperty ano = new SimpleStringProperty();

	// vista

	@FXML
	private HBox view;

	@FXML
	private ComboBox<Integer> dayCombo;

	@FXML
	private ComboBox<String> monthCombo;

	@FXML
	private ComboBox<String> yearCombo;

	public DateChooser() {
		super();
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/DateChooserView.fxml"));
			loader.setController(this);
			loader.setRoot(this);
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ListProperty<Integer> listaDays = new SimpleListProperty<>(FXCollections.observableArrayList());
		ListProperty<String> listMonths = new SimpleListProperty<>(FXCollections.observableArrayList());
		ListProperty<String> listYears = new SimpleListProperty<>(FXCollections.observableArrayList());

		mesProperty().bind(monthCombo.getSelectionModel().selectedIndexProperty().add(1));
		diaProperty().bind(dayCombo.getSelectionModel().selectedItemProperty());
		anoProperty().bind(yearCombo.getSelectionModel().selectedItemProperty());
		
		
		
		dayCombo.itemsProperty().bind(listaDays);
		monthCombo.itemsProperty().bind(listMonths);
		yearCombo.itemsProperty().bind(listYears);
		
		for (int i = 1900; i < Calendar.getInstance().get(Calendar.YEAR) + 1; i++) {
			listYears.add("" + i);
		}
		listMonths.addAll("Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre",
				"Octubre", "Noviembre", "Diciembre");
		for (int i = 1; i <= 31; i++) {
			listaDays.add(i);
		}
		
		dateProperty.addListener((o,ov,nv)->{
			monthCombo.getSelectionModel().select(listMonths.get(nv.getMonthValue()-1));
			dayCombo.getSelectionModel().select(nv.getDayOfMonth()-1);
			yearCombo.getSelectionModel().select(listYears.indexOf(nv.getYear()+""));
		});
		
		diaProperty().addListener((o, ov, nv) -> {
			if(nv.intValue()!=0 && getMes()!=0)
			dateProperty.set(LocalDate.of(getDateProperty().getYear(),getMes(), nv.intValue()));
		});

		mesProperty().addListener((o, ov, nv) -> {
			listaDays.clear();

			if (getMes() == 4 || getMes() == 6 || getMes() == 9 || getMes() == 11) {
				for (int i = 1; i <= 30; i++) {
					listaDays.add(i);
				}
			} else if (getMes() == 2) {
				if (Year.of(getAnoConvertido()).isLeap() && getAnoConvertido() != 0) {
					for (int i = 1; i <= 29; i++) {
						listaDays.add(i);
					}
				} else {
					for (int i = 1; i <= 28; i++) {
						listaDays.add(i);
					}
				}
			} else {
				for (int i = 1; i <= 31; i++) {
					listaDays.add(i);
				}
			}
			dayCombo.getSelectionModel().select(0);
		});

		anoProperty().addListener((o, ov, nv) -> {
			try {
				setAnoConvertido(Integer.parseInt(nv));
			} catch (NumberFormatException e) {
				listaDays.clear();
			}
		});

		anoConvertidoProperty().addListener((o, ov, nv) -> {
			try {
				if (getMes() == 2) {
					listaDays.clear();
					if (Year.of(getAnoConvertido()).isLeap()) {
						for (int i = 1; i <= 29; i++) {
							listaDays.add(i);
						}
					} else {
						for (int i = 1; i <= 28; i++) {
							listaDays.add(i);
						}
					}
					dayCombo.getSelectionModel().select(0);
				}
			} catch (NullPointerException e) {
			}
			dateProperty.set(LocalDate.of(getAnoConvertido(), getDateProperty().getMonthValue(), getDateProperty().getDayOfMonth()));
		});

		dateProperty.set(LocalDate.now());
	}

	public final ObjectProperty<LocalDate> datePropertyProperty() {
		return this.dateProperty;
	}

	public final LocalDate getDateProperty() {
		return this.datePropertyProperty().get();
	}

	public final void setDateProperty(final LocalDate dateProperty) {
		this.datePropertyProperty().set(dateProperty);
	}

	public final IntegerProperty diaProperty() {
		return this.dia;
	}

	public final int getDia() {
		return this.diaProperty().get();
	}

	public final void setDia(final int dia) {
		this.diaProperty().set(dia);
	}

	public final IntegerProperty anoConvertidoProperty() {
		return this.anoConvertido;
	}

	public final int getAnoConvertido() {
		return this.anoConvertidoProperty().get();
	}

	public final void setAnoConvertido(final int anoConvertido) {
		this.anoConvertidoProperty().set(anoConvertido);
	}

	public final StringProperty anoProperty() {
		return this.ano;
	}

	public final String getAno() {
		return this.anoProperty().get();
	}

	public final void setAno(final String ano) {
		this.anoProperty().set(ano);
	}

	public final IntegerProperty mesProperty() {
		return this.mes;
	}

	public final int getMes() {
		return this.mesProperty().get();
	}

	public final void setMes(final int mes) {
		this.mesProperty().set(mes);
	}

}
