package com.clouway.task4;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Deyan Sadinov <sadinov88@gmail.com>
 */
public interface RowFetcher<T> {

  T fetchRow(ResultSet rs);
}
