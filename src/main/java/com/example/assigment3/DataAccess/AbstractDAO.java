package com.example.assigment3.DataAccess;

import com.example.assigment3.Connection.ConnectionFactory;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AbstractDAO<T> {

    public final Class<T> type;

    @SuppressWarnings("unchecked")
    public AbstractDAO() {
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public T findById(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectQuery("ID");
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            return createObjects(resultSet).get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    private String createSelectQuery(String field) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" * ");
        sb.append(" FROM ");
        sb.append(type.getSimpleName());
        sb.append(" WHERE " + field + " =?");
        return sb.toString();
    }

    public List<T> getAll(){
        String query = "SELECT * from `" + type.getSimpleName() + "`";
        //System.out.println(query);
        Connection connection = ConnectionFactory.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            return createObjects(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void insert(T t){
        String query = generateInsertQuery();
        System.out.println(query);
        try{
            PreparedStatement statement = ConnectionFactory.getConnection().prepareStatement(query);
            int index=1;
            for(Field field:type.getDeclaredFields()){
                field.setAccessible(true);
                if("id".equals(field.getName())){
                    continue;
                }
                Object value = field.get(t);
                statement.setObject(index++,value);
            }
            statement.execute();
        }
        catch (IllegalAccessException | SQLException e){
            e.printStackTrace();
        }
    }

    private String generateInsertQuery() {
        StringBuilder query = new StringBuilder( "INSERT INTO `" + type.getSimpleName() + "` (");
        for(Field field:type.getDeclaredFields()){
            if("id".equals(field.getName())){
                continue;
            }
            query.append(field.getName());
            query.append(",");
        }
        query.deleteCharAt(query.length()-1);
        query.append(") VALUES (");

        for (int i=1;i<=type.getDeclaredFields().length-1;i++){
            query.append("?,");
        }
        query.deleteCharAt(query.length()-1);
        query.append(")");

        return query.toString();
    }

    public void update(T t){
        String query = generateUpdateQuery();
        System.out.println(query);
        try{
            PreparedStatement statement = ConnectionFactory.getConnection().prepareStatement(query);
            int index=1;
            for(Field field:type.getDeclaredFields()){
                field.setAccessible(true);
                if("id".equals(field.getName())){
                    continue;
                }
                Object value = field.get(t);
                statement.setObject(index++,value);
            }
            Field idField = type.getDeclaredField("id");
            idField.setAccessible(true);
            statement.setObject(index,idField.get(t));
            statement.execute();
        }
        catch (IllegalAccessException | SQLException | NoSuchFieldException e){
            e.printStackTrace();
        }

    }

    private String generateUpdateQuery() {
        StringBuilder query = new StringBuilder("UPDATE `"+ type.getSimpleName() +"` SET ");
        for(Field field:type.getDeclaredFields()){
            if("id".equals(field.getName())){
                continue;
            }
            query.append(field.getName() + "=?,");
        }
        query.deleteCharAt(query.length()-1);
        query.append(" WHERE ID =?");
        return query.toString();
    }

    public void delete(int id){
        String query = "DELETE FROM "+ type.getSimpleName() + " WHERE ID = ?";

        try{
            PreparedStatement statement = ConnectionFactory.getConnection().prepareStatement(query);
            statement.setInt(1,id);
            statement.execute();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    private List<T> createObjects(ResultSet resultSet) {

        List<T> list = new ArrayList<T>();

        Constructor[] ctors = type.getDeclaredConstructors();
        Constructor ctor = null;
        for (int i = 0; i < ctors.length; i++) {
            ctor = ctors[i];
            if (ctor.getGenericParameterTypes().length == 0)//imi caut constructorul cu 0 parametrii
                break;
        }

        try {
            while (resultSet.next()) {
                ctor.setAccessible(true); //Setez constructorul vizibil, public.
                T instance = (T) ctor.newInstance();//Creez o instanta a obiectului respectiv neinitializata pt ca constructorul are 0 parametrii
                for (Field field : type.getDeclaredFields()) {//Iteram pe filedurile tipului meu
                    String fieldName = field.getName();//Iau numele
                    Object value = resultSet.getObject(fieldName);//Pt ca numele corespunde 1 la 1 cu numele fieldului din sql atunci pot sa folosesc numele fieldului pentru a obtine valoarea corespunzatoare direct din result
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, type);
                    Method method = propertyDescriptor.getWriteMethod();//Returneaza setterul fieldului
                    method.invoke(instance, value);//Invoc setterul pt a modifica instanta cu valoarea
                }
                list.add(instance);//adaug in lista obiectul respectiv
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        return list;
    }

}
