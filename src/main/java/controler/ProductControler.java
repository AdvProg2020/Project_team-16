package controler;

import ModelPackage.Product.Comment;
import ModelPackage.Product.CommentStatus;
import ModelPackage.Product.Product;
import ModelPackage.System.exeption.product.NoSuchAProductException;
import View.PrintModels.CommentPM;
import View.PrintModels.FullProductPM;
import View.PrintModels.MiniProductPM;

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
        Comment[] comments = productManager.showComments(productId);
        List<CommentPM> commentPMs = new ArrayList<>();
        for (Comment comment : comments) {
            commentPMs.add(new CommentPM(comment.getUserId(),
                    comment.getTitle(),
                    comment.getText()));
        }
        return commentPMs;
    }

    public FullProductPM viewAttributes(int productId) throws NoSuchAProductException {
        return createFullProductPM(productId);
    }

    public FullProductPM[] compareProducts(String[] data) throws NoSuchAProductException {
        int firstProductId = Integer.parseInt(data[0]);
        int secondProductId = Integer.parseInt(data[1]);
        FullProductPM[] fullProductPMs = new FullProductPM[2];
        fullProductPMs[0] = createFullProductPM(firstProductId);
        fullProductPMs[1] = createFullProductPM(secondProductId);
        return fullProductPMs;
    }

    public MiniProductPM digest(int productId) throws NoSuchAProductException {
        Product product = productManager.findProductById(productId);
        return createMiniProductPM(product);
    }

    private FullProductPM createFullProductPM(int productId) throws NoSuchAProductException {
        Product product = productManager.findProductById(productId);
        return new FullProductPM(createMiniProductPM(product),
                productManager.allFeaturesOf(product));
    }

    private MiniProductPM createMiniProductPM(Product product) {
        return new MiniProductPM(product.getName(), product.getId(),
                product.getCategory().getName(),
                product.getPrices(), product.getCompany(),
                product.getTotalScore(), product.getDescription());
    }

}
