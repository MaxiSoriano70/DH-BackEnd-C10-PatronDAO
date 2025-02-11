package dao.implementacion;

import dao.IDao;
import db.H2Connection;
import model.Medicamento;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicamentoIDaoH2 implements IDao<Medicamento> {
    public static Logger LOGGER = Logger.getLogger(MedicamentoIDaoH2.class);
    public static String SQL_INSERT = "INSERT INTO MEDICAMENTOS VALUES (DEFAULT, ?, ?, ? ,?, ?);";
    public static  String SQL_SELECT = "SELECT * FROM MEDICAMENTOS WHERE NOMBRE = ?";
    public static  String SQL_SELECT_ID = "SELECT * FROM MEDICAMENTOS WHERE ID = ?";
    public static  String SQL_SELECT_TODOS = "SELECT * FROM MEDICAMENTOS";
    @Override
    public Medicamento registrar(Medicamento medicamento) {
        Connection connection = null;
        Medicamento medicamentoRetornar = null;
        try{
            connection = H2Connection.getConnection();
            connection.setAutoCommit(false);

            PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, medicamento.getCodigo());
            preparedStatement.setString(2, medicamento.getNombre());
            preparedStatement.setString(3, medicamento.getLaboratorio());
            preparedStatement.setInt(4, medicamento.getCantidad());
            preparedStatement.setDouble(5, medicamento.getPrecio());
            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            while (resultSet.next()){
                Integer id = resultSet.getInt(1);
                medicamentoRetornar = new Medicamento(id, medicamento.getCodigo(), medicamento.getNombre(), medicamento.getLaboratorio(), medicamento.getCantidad(), medicamento.getPrecio());
            }
            LOGGER.info("Medicamento persistido = "+ medicamentoRetornar);

            connection.commit();
            connection.setAutoCommit(true);
        }catch (Exception e){
            if(connection!=null){
                try{
                    connection.rollback();
                }catch (SQLException ex) {
                    LOGGER.error(ex.getMessage());
                    ex.printStackTrace();
                }
            }
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
        return medicamentoRetornar;
    }

    @Override
    public Medicamento buscarPorCampo(String campo) {
        Connection connection = null;
        Medicamento medicamentoRetornar = null;
        try {
            connection = H2Connection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT);
            String auxCampo = campo.toUpperCase();
            preparedStatement.setString(1, auxCampo);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                Integer id = resultSet.getInt(1);
                Integer codigo = resultSet.getInt(2);
                String nombre = resultSet.getString(3);
                String laboratorio = resultSet.getString(4);
                int cantidad = resultSet.getInt(5);
                double precio = resultSet.getDouble(6);
                medicamentoRetornar = new Medicamento(id, codigo, nombre, laboratorio, cantidad, precio);
            }
            LOGGER.info("El medicamento encontrado es: "+ medicamentoRetornar);
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
        return medicamentoRetornar;
    }

    @Override
    public Medicamento buscarPorId(Integer id) {
        Connection connection = null;
        Medicamento medicamentoRetornar = null;
        try {
            connection = H2Connection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_ID);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                Integer idMedicamento = resultSet.getInt(1);
                Integer codigo = resultSet.getInt(2);
                String nombre = resultSet.getString(3);
                String laboratorio = resultSet.getString(4);
                int cantidad = resultSet.getInt(5);
                double precio = resultSet.getDouble(6);
                medicamentoRetornar = new Medicamento(idMedicamento, codigo, nombre, laboratorio, cantidad, precio);
            }
            LOGGER.info("El medicamento encontrado es: "+ medicamentoRetornar);
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
        return medicamentoRetornar;
    }

    @Override
    public List<Medicamento> buscarTodos() {
        Connection connection = null;
        Medicamento medicamento = null;
        List<Medicamento> medicamentos = new ArrayList<>();
        try {
            connection = H2Connection.getConnection();
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(SQL_SELECT_TODOS);

            while (resultSet.next()){
                medicamento = new Medicamento(
                        resultSet.getInt(1),
                        resultSet.getInt(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getInt(5),
                        resultSet.getDouble(6)
                );
                LOGGER.info("Medicamento devuelto: "+medicamento);
                medicamentos.add(medicamento);
            }

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
        return medicamentos;
    }
}
