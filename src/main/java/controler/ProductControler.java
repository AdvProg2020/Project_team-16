package controler;

import ModelPackage.Product.Comment;
import ModelPackage.Product.CommentStatus;
import ModelPackage.Product.Product;
import ModelPackage.System.exeption.product.NoSuchAProductException;
import View.PrintModels.CommentPM;

import java.util.ArrayList;
import java.util.List;

public class ProductControler extends Controller{

    public void assignComment(String[] data) throws NoSuchAProductException {
        String userId = data[0];
        String commentTitle = data[1];
        String commentText = data[2];
        int productId = Integer.parseInt(data[3]);
        // TODO : check if user bought this product
        Comment comment = new Comment(userId, commentTitle, commentText,
                CommentStatus.NOT_VERIFIED, false);
        comment.setProduct(productManager.findProductById(productId));
        csclManager.createComment(comment);
    }

    public List<CommentPM> viewProductComments(int productId) throws NoSuchAProductException {
        Product product = productManager.findProductById(productId);
        List<Comment> comments = product.getAllComments();
        List<CommentPM> commentPMs = new ArrayList<>();
        for (Comment comment : comments) {
            commentPMs.add(new CommentPM(comment.getUserId(),
                    comment.getTitle(),
                    comment.getText()));
        }
        return commentPMs;
    }

}
