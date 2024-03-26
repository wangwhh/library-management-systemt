package com.example.spring;

import com.example.spring.entities.*;
import com.example.spring.queries.ApiResult;
import com.example.spring.queries.BookQueryConditions;
import com.example.spring.utils.DBInitializer;
import com.example.spring.utils.DatabaseConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import static com.example.spring.entities.Card.CardType.values;

public class LibraryManagementSystemImpl implements LibraryManagementSystem {

    private final DatabaseConnector connector;

    public LibraryManagementSystemImpl(DatabaseConnector connector) {
        this.connector = connector;
    }

    @Override
    public ApiResult storeBook(Book book) {
        Connection conn = connector.getConn();
        ResultSet rs;
        int id = 0;
        String sql = "insert into book( category, title, press, publish_year, " +
                "author, price, stock) values(?,?,?,?,?,?,?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, book.getCategory());
            ps.setString(2, book.getTitle());
            ps.setString(3, book.getPress());
            ps.setInt(4, book.getPublishYear());
            ps.setString(5, book.getAuthor());
            ps.setDouble(6, book.getPrice());
            ps.setInt(7, book.getStock());

            ps.executeUpdate();
            conn.commit();
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                id = rs.getInt(1);
                book.setBookId(id);
            }

            return new ApiResult(true, "storeBook Success.");
        } catch (SQLException e) {
            e.printStackTrace();
            return new ApiResult(false, "storeBook Fail.");
        }

    }

    @Override
    public ApiResult incBookStock(int bookId, int deltaStock) {
        Connection conn = connector.getConn();
        ResultSet rs;
        int stock = 0;
        String get_stock = "select stock from book where book_id=?";
        String sql = "update book set stock = stock + ? where book_id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(get_stock);
            ps.setInt(1, bookId);
            rs = ps.executeQuery();
            if (rs.next())
                stock = rs.getInt("stock");
            if (stock + deltaStock < 0) {
                return new ApiResult(false, "The stock cannot be negative!");
            }

            ps = conn.prepareStatement(sql);
            ps.setInt(1, deltaStock);
            ps.setInt(2, bookId);
            ps.executeUpdate();
            conn.commit();
            return new ApiResult(true, "incBookStock Success.");
        } catch (SQLException e) {
            e.printStackTrace();
            return new ApiResult(false, "incBookStock Fail.");
        }
    }

    @Override
    public ApiResult storeBook(List<Book> books) {
        Connection conn = connector.getConn();
        ResultSet rs;
        int id = 0;
        ListIterator<Book> it = books.listIterator();
        String sql = "insert into book( category, title, press, publish_year, " +
                "author, price, stock) values(?,?,?,?,?,?,?)";
        Book book;
        try {
            while (it.hasNext()) {
                book = it.next();
                PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                ps.setString(1, book.getCategory());
                ps.setString(2, book.getTitle());
                ps.setString(3, book.getPress());
                ps.setInt(4, book.getPublishYear());
                ps.setString(5, book.getAuthor());
                ps.setDouble(6, book.getPrice());
                ps.setInt(7, book.getStock());

                ps.executeUpdate();
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    id = rs.getInt(1);
                    book.setBookId(id);
                }
            }
            conn.commit();
            return new ApiResult(true, "storeBook List Success.");
        } catch (SQLException e) {
            e.printStackTrace();
            return new ApiResult(false, "storeBook List Fail.");
        }

    }

    @Override
    public ApiResult removeBook(int bookId) {
        Connection conn = connector.getConn();
        ResultSet rs;
        String sql = "delete from book where book_id = ?";
        String query_borrow = "select return_time from borrow where book_id=?";
        try {
            PreparedStatement ps = conn.prepareStatement(query_borrow);
            ps.setInt(1, bookId);
            rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getLong("return_time") == 0) {
                    return new ApiResult(false, "还有人未归还这本书");
                }
            }

            ps = conn.prepareStatement(sql);
            ps.setInt(1, bookId);
            ps.executeUpdate();
            conn.commit();
            return new ApiResult(true, "removeBook Success.");
        } catch (SQLException e) {
            e.printStackTrace();
            return new ApiResult(false, "removeBook Fail.");
        }
    }

    @Override
    public ApiResult modifyBookInfo(Book book) {

        Connection conn = connector.getConn();
        int id = book.getBookId();
        String sql = "update book set category=?, title=?, press=?, publish_year=?, " +
                "author=?, price=?, stock=? where book_id = " + id;
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, book.getCategory());
            ps.setString(2, book.getTitle());
            ps.setString(3, book.getPress());
            ps.setInt(4, book.getPublishYear());
            ps.setString(5, book.getAuthor());
            ps.setDouble(6, book.getPrice());
            ps.setInt(7, book.getStock());
            ps.executeUpdate();
            conn.commit();
            return new ApiResult(true, "modifyBookInfo Success.");
        } catch (SQLException e) {
            e.printStackTrace();
            return new ApiResult(false, "modifyBookInfo Fail.");
        }
    }

    @Override
    public List<Book> queryBook(BookQueryConditions conditions) {
        Connection conn = connector.getConn();
        ResultSet rs;
        String sql = "select * from book where ";
        int flag = 0;
        List<Book> books = new ArrayList<>();
        try {
            if (conditions.getCategory() != "" && conditions.getCategory() != null) {
                sql += "category = '" + conditions.getCategory() + "'";
                flag = 1;
            }
            if (conditions.getTitle() != "" && conditions.getTitle() != null) {
                if (flag == 1) sql += " and ";
                else flag = 1;
                sql += "title like '%" + conditions.getTitle() + "%'";
            }
            if (conditions.getPress() != "" && conditions.getPress() != null) {
                if (flag == 1) sql += " and ";
                else flag = 1;
                sql += "press like '%" + conditions.getPress() + "%'";
            }
            if (conditions.getMinPublishYear() != null) {
                if (flag == 1) sql += " and ";
                else flag = 1;
                sql += "publish_year>" + conditions.getMinPublishYear();
            }
            if (conditions.getMaxPublishYear() != null) {
                if (flag == 1) sql += " and ";
                else flag = 1;
                sql += "publish_year<" + conditions.getMaxPublishYear();
            }
            if (conditions.getAuthor() != "" && conditions.getAuthor() != null) {
                if (flag == 1) sql += " and ";
                else flag = 1;
                sql += "author like '%" + conditions.getAuthor() + "%'";
            }
            if (conditions.getMinPrice() != null) {
                if (flag == 1) sql += " and ";
                else flag = 1;
                sql += "`price`>" + conditions.getMinPrice();
            }
            if (conditions.getMaxPrice() != null) {
                if (flag == 1) sql += " and ";
                else flag = 1;
                sql += "`price`<" + conditions.getMaxPrice();
            }
            if (flag != 1) {
                sql = "select * from book";
            }
            sql += " order by " + conditions.getSortBy() + " " + conditions.getSortOrder();

            PreparedStatement ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Book book = new Book(rs.getString("category"), rs.getString("title"),
                        rs.getString("press"), rs.getInt("publish_Year"),
                        rs.getString("author"), rs.getDouble("price"), rs.getInt("stock"));
                book.setBookId(rs.getInt("book_id"));
                if (books != null)
                    books.add(book);
            }
            return books;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public ApiResult borrowBook(Borrow borrow) {
        Connection conn = connector.getConn();
        ResultSet rs;
        String query_borrow = "select return_time from borrow where card_id=? and book_id=?";
        String query_stock = "select stock from book where book_id = ?";
        String insertion = "insert into borrow(card_id, book_id, borrow_time) values(?,?,?)";
        String update_stock = "update book set stock=stock-1 where book_id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(query_borrow);
            ps.setInt(1, borrow.getCardId());
            ps.setInt(2, borrow.getBookId());
            rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getLong("return_time") == 0)
                    return new ApiResult(false, "你借了这本书且尚未归还!");
            }

            // get the stock
            ps = conn.prepareStatement(query_stock);
            ps.setInt(1, borrow.getBookId());
            rs = ps.executeQuery();
            if (rs.next()) {
                int stock = rs.getInt("stock");
                if (stock == 0) {
                    return new ApiResult(false, "库存为0!");
                }
            }

            // insert a line into `borrow`
            ps = conn.prepareStatement(insertion);
            ps.setInt(1, borrow.getCardId());
            ps.setInt(2, borrow.getBookId());
            ps.setLong(3, borrow.getBorrowTime());
            ps.executeUpdate();

            // update stock in `book`
            ps = conn.prepareStatement(update_stock);
            ps.setInt(1, borrow.getBookId());
            ps.executeUpdate();

            conn.commit();
            return new ApiResult(true, "borrowBook Success.");
        } catch (SQLException e) {
            e.printStackTrace();
            return new ApiResult(false, "图书编号或借书证不存在!");
        }
    }

    @Override
    public ApiResult returnBook(Borrow borrow) {
        Connection conn = connector.getConn();
        ResultSet rs;
        String update_return = "update borrow set return_time=? where book_id=? and card_id=?";
        String update_stock = "update book set stock=stock+1 where book_id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(update_return);
            ps.setLong(1, borrow.getReturnTime());
            ps.setInt(2, borrow.getBookId());
            ps.setInt(3, borrow.getCardId());
            ps.executeUpdate();

            ps = conn.prepareStatement(update_stock);
            ps.setInt(1, borrow.getBookId());
            ps.executeUpdate();

            conn.commit();
            return new ApiResult(true, "returnBook Success.");
        } catch (SQLException e) {
            e.printStackTrace();
            return new ApiResult(false, "returnBook Fail.");
        }
    }

    @Override
    public List<Borrow> showBorrow() {
        Connection conn = connector.getConn();
        ResultSet rs;
        String sql = "select * from borrow";
        List<Borrow> borrows = new ArrayList<>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Borrow borrow = new Borrow(rs.getInt("book_id"), rs.getInt("card_id"));
                borrow.setBorrowTime(rs.getLong("borrow_time"));
                borrow.setReturnTime(rs.getLong("return_time"));
                borrows.add(borrow);
            }
            return borrows;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Borrow> showBorrowHistory(int cardId) {
        Connection conn = connector.getConn();
        ResultSet rs;
        String sql="select * from borrow where card_id=?";
        List<Borrow> borrows = new ArrayList<>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, cardId);
            rs=ps.executeQuery();
            while(rs.next()) {
                Borrow borrow = new Borrow(rs.getInt("book_id"), rs.getInt("card_id"));
                borrow.setBorrowTime(rs.getLong("borrow_time"));
                borrow.setReturnTime(rs.getLong("return_time"));
                borrows.add(borrow);
            }
            return borrows;
        } catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ApiResult registerCard(Card card) {
        Connection conn = connector.getConn();
        ResultSet rs;
        int id = 0;
        String sql="insert into card(name, department, type) values(?,?,?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, card.getName());
            ps.setString(2, card.getDepartment());
            ps.setString(3, card.getType().getStr());

            ps.executeUpdate();
            conn.commit();
            rs = ps.getGeneratedKeys();
            if(rs.next()) {
                id = rs.getInt(1);
                card.setCardId(id);
            }
            return new ApiResult(true, "registerCard Success.");
        } catch(SQLException e) {
            e.printStackTrace();
            return new ApiResult(false, "registerCard Fail.");
        }
    }

    @Override
    public ApiResult removeCard(int cardId) {
        Connection conn = connector.getConn();
        ResultSet rs;
        String sql="delete from card where card_id = ?";
        String query_borrow="select return_time from borrow where card_id=?";
        try {
            PreparedStatement ps = conn.prepareStatement(query_borrow);
            ps.setInt(1, cardId);
            rs = ps.executeQuery();
            while(rs.next()){
                if(rs.getLong("return_time")==0)
                    return new ApiResult(false, "还有未归还的书!");
            }

            ps = conn.prepareStatement(sql);
            ps.setInt(1, cardId);
            ps.executeUpdate();
            conn.commit();
            return new ApiResult(true, "removeCard Success.");
        } catch(SQLException e) {
            e.printStackTrace();
            return new ApiResult(false, "removeCard Fail.");
        }
    }

    @Override
    public List<Card> showCards() {
        Connection conn = connector.getConn();
        ResultSet rs;
        String sql="select * from card";
        List<Card> cards = new ArrayList<>();
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                Card card=new Card(rs.getInt("card_id"), rs.getString("name"),
                        rs.getString("department"), values(rs.getString("type")));
                cards.add(card);
            }
        } catch (java.sql.SQLException e){
            e.printStackTrace();
            return null;
        }
        return cards;
    }

    @Override
    public ApiResult resetDatabase() {
        Connection conn = connector.getConn();
        try {
            Statement stmt = conn.createStatement();
            DBInitializer initializer = connector.getConf().getType().getDbInitializer();
            stmt.addBatch(initializer.sqlDropBorrow());
            stmt.addBatch(initializer.sqlDropBook());
            stmt.addBatch(initializer.sqlDropCard());
            stmt.addBatch(initializer.sqlCreateCard());
            stmt.addBatch(initializer.sqlCreateBook());
            stmt.addBatch(initializer.sqlCreateBorrow());
            stmt.executeBatch();
            commit(conn);
        } catch (Exception e) {
            rollback(conn);
            return new ApiResult(false, e.getMessage());
        }
        return new ApiResult(true, null);
    }

    private void rollback(Connection conn) {
        try {
            conn.rollback();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void commit(Connection conn) {
        try {
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
