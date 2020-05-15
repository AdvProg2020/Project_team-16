package controler;

import ModelPackage.Product.Comment;
import ModelPackage.Product.CommentStatus;
import ModelPackage.System.exeption.product.NoSuchAProductException;

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

}
