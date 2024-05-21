package service;

import dao.implementacion.MedicamentoIDaoH2;
import model.Medicamento;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MedicamentoServiceTest {

    @BeforeAll
    static void crearTablas(){
        Connection connection = null;
        try {
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection("jdbc:h2:~/clase10medicamentos;INIT=RUNSCRIPT from 'create.sql'","sa","sa");
        }catch (Exception e){
            LOGGER.error(e.getMessage());
            e.printStackTrace();
        }finally {
            try {
                connection.close();
            } catch (SQLException e) {
                LOGGER.error(e.getMessage());
                e.printStackTrace();
            }
        }
    }
    public static Logger LOGGER = Logger.getLogger(MedicamentoServiceTest.class);

    @Test
    @DisplayName("Testear que un medicamento persiste en la BD")
    void testerMedicamentoEnBD(){
        Medicamento medicamento = new Medicamento(1234, "Covid 20", "Pfizer", 30, 500);
        MedicamentoService medicamentoService = new MedicamentoService(new MedicamentoIDaoH2());
        Medicamento medicamentoPersistido = medicamentoService.registrarMedicamento(medicamento);

        assertNotNull(medicamentoPersistido);
    }

    @Test
    @DisplayName("Testear que un medicamento pasandole un nombre")
    void testearMedicamentoPorNombre(){
        MedicamentoService medicamentoService = new MedicamentoService(new MedicamentoIDaoH2());
        String nombre = "covid-19";
        Medicamento medicamentoEncontrado =  medicamentoService.buscarPorNombre(nombre);

        assertEquals("COVID-19", medicamentoEncontrado.getNombre());
    }

    @Test
    @DisplayName("Testear quenho retorne un medicamento pasandole un nombre que no existe.")
    void testearMedicamentoPorNombreNoExistente(){
        MedicamentoService medicamentoService = new MedicamentoService(new MedicamentoIDaoH2());
        String nombre = "Covid 25";
        Medicamento medicamentoEncontrado =  medicamentoService.buscarPorNombre(nombre);

        assertNull(medicamentoEncontrado);
    }

    @Test
    @DisplayName("Testear quenho retorne todos los medicamentos.")
    void testearTodosLosMedicamentos(){
        MedicamentoService medicamentoService = new MedicamentoService(new MedicamentoIDaoH2());
        List <Medicamento> medicamentosRecibidos = medicamentoService.buscarTodos();

        assertEquals(2, medicamentosRecibidos.size());
    }

    @Test
    @DisplayName("Testear que un medicamento pasandole un id")
    void testearMedicamentoPorId(){
        MedicamentoService medicamentoService = new MedicamentoService(new MedicamentoIDaoH2());
        Integer id = 2;
        Medicamento medicamentoEncontrado =  medicamentoService.buscarPorId(2);

        assertEquals(2, medicamentoEncontrado.getId());
    }
}