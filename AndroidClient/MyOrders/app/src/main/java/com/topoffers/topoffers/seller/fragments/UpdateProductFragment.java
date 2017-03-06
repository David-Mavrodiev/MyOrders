package com.topoffers.topoffers.seller.fragments;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.topoffers.data.base.IData;
import com.topoffers.data.base.IImageData;
import com.topoffers.data.models.Headers;
import com.topoffers.data.models.RequestFormBodyKeys;
import com.topoffers.topoffers.R;
import com.topoffers.topoffers.common.fragments.DialogFragment;
import com.topoffers.topoffers.common.fragments.LoadingFragment;
import com.topoffers.topoffers.common.helpers.AuthenticationHelpers;
import com.topoffers.topoffers.common.models.AuthenticationCookie;
import com.topoffers.topoffers.common.models.Product;
import com.topoffers.topoffers.seller.activities.SellerProductsListActivity;
import com.topoffers.topoffers.seller.helpers.ImageHandler;
import com.topoffers.topoffers.seller.types.UpdateProductType;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import io.reactivex.functions.Consumer;
import pl.aprilapps.easyphotopicker.EasyImage;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateProductFragment extends Fragment {
    private View root;
    private IData<Product> productData;
    private IImageData imageData;
    private AuthenticationCookie cookie;

    private UpdateProductType mode;
    private int productId;

    private File imageFile;

    public UpdateProductFragment() {
        // Required empty public constructor
    }

    public static UpdateProductFragment create(IData<Product> productData, IImageData imageData, AuthenticationCookie cookie, int productId) {
        UpdateProductFragment updateProductFragment = new UpdateProductFragment();
        updateProductFragment.setProductData(productData);
        updateProductFragment.setImageData(imageData);
        updateProductFragment.setCookie(cookie);
        updateProductFragment.setProductId(productId);
        if (productId == 0) {
            updateProductFragment.setMode(UpdateProductType.CREATE);
        } else {
            updateProductFragment.setMode(UpdateProductType.EDIT);
        }
        return updateProductFragment;
    }

    public void setProductData(IData<Product> productData) {
        this.productData = productData;
    }

    public void setImageData(IImageData imageData) {
        this.imageData = imageData;
    }

    public void setCookie(AuthenticationCookie cookie) {
        this.cookie = cookie;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setMode(UpdateProductType mode) {
        this.mode = mode;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_update_product_fragament, container, false);
        this.initProductDetails();
        return root;
    }

    private void initProductDetails() {
        if (this.mode == UpdateProductType.EDIT) {
            this.initEditProduct();
        }

        final UpdateProductFragment fragment = this;
        final Context context = this.getContext();

        // Hide initial Image view
        ImageView imProductImage = (ImageView) root.findViewById(R.id.im_product_image);
        imProductImage.setVisibility(View.GONE);

        // Upload picture camera
        Button btnUploadPictureCamera = (Button) root.findViewById(R.id.btn_update_product_upload_picture_camera);
        btnUploadPictureCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EasyImage.openCamera(fragment, EasyImage.REQ_SOURCE_CHOOSER);
            }
        });

        // Upload picture camera
        Button btnUploadPictureGallery = (Button) root.findViewById(R.id.btn_update_product_upload_picture_gallery);
        btnUploadPictureGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EasyImage.openGallery(fragment, EasyImage.REQ_PICK_PICTURE_FROM_GALLERY);
            }
        });

        Button btnUpdateProduct = (Button) root.findViewById(R.id.btn_update_product);
        btnUpdateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String title = ((EditText) root.findViewById(R.id.et_update_product_title)).getText().toString();
                String description = ((EditText) root.findViewById(R.id.et_update_product_description)).getText().toString();

                int quantity = -1;
                String quantityAsString = ((EditText) root.findViewById(R.id.et_update_product_quantity)).getText().toString();
                if (!quantityAsString.equals("")) {
                    quantity = Integer.parseInt(quantityAsString);
                }

                double price = -1;
                String priceAsDouble = ((EditText) root.findViewById(R.id.et_update_product_price)).getText().toString();
                if (!priceAsDouble.isEmpty()) {
                    try {
                        price = Double.parseDouble(priceAsDouble);
                    } catch (Exception e) {
                        (DialogFragment.create(context, "Please enter valid price", 1)).show();
                    }
                }

                String errorMessage = "";
                if (title.isEmpty()) {
                    errorMessage = "Please enter product title";
                } else if (price < 0) {
                    errorMessage = "Please enter product price";
                } else if (quantity < 0) {
                    errorMessage = "Please enter product quantity";
                } else if (imageFile == null && mode == UpdateProductType.CREATE) {
                    errorMessage = "Please take/upload product picture";
                } else {
                    Product product = new Product(title, price, quantity, null, description, imageFile);
                    handleUpdateProduct(product);
                }

                if (!errorMessage.isEmpty()) {
                    (DialogFragment.create(context, errorMessage, 1)).show();
                }
            }
        });
    }

    private void handleUpdateProduct(Product product) {
        final Context context = this.getContext();
        Headers headers = AuthenticationHelpers.getAuthenticationHeaders(cookie);
        List<String> requestFormBodyKeysArrays = new ArrayList<String>(Arrays.asList("title", "price", "quantity", "description"));

        final LoadingFragment loadingFragment = LoadingFragment.create(this.getContext(), "Creating product...");
        loadingFragment.show();

        if (this.mode ==  UpdateProductType.EDIT) {
            productData.edit(productId, product, headers)
                .subscribe(new Consumer<Product>() {
                    @Override
                    public void accept(Product product) throws Exception {
                        // Notify user
                        String notifyMessage = String.format("%s was successfully edited.", product.getTitle());
                        (DialogFragment.create(context, notifyMessage, 1)).show();

                        loadingFragment.hide();

                        // Redirects to products list
                        Intent intent = new Intent(context, SellerProductsListActivity.class);
                        context.startActivity(intent);
                    }
                });
        } else {
            RequestFormBodyKeys requestFormBodyKeys = new RequestFormBodyKeys(requestFormBodyKeysArrays);
            productData.addMultipartWithImage(product, requestFormBodyKeys, headers)
                .subscribe(new Consumer<Product>() {
                    @Override
                    public void accept(Product product) throws Exception {
                        String notifyMessage = String.format("%s was successfully added.", product.getTitle());
                        (DialogFragment.create(context, notifyMessage, 1)).show();

                        loadingFragment.hide();

                        // Redirects to products list
                        Intent intent = new Intent(context, SellerProductsListActivity.class);
                        context.startActivity(intent);
                    }
                });
        }
    }

    private void initEditProduct() {
        Button btnUpdateProduct = (Button) root.findViewById(R.id.btn_update_product);
        btnUpdateProduct.setText("Update Product");

        // Hide upload image text
        TextView tvUploadCreateImage = (TextView) root.findViewById(R.id.tv_upload_create_image);
        tvUploadCreateImage.setVisibility(View.GONE);

        // Hide upload image action buttons
        LinearLayout actionButtonsLayout = (LinearLayout) root.findViewById(R.id.action_image_buttons_layout);
        actionButtonsLayout.setVisibility(View.GONE);

        final LoadingFragment loadingFragment = LoadingFragment.create(this.getContext(), "Getting product data...");
        loadingFragment.show();

        Headers headers = AuthenticationHelpers.getAuthenticationHeaders(cookie);
        productData.getById(this.productId, headers)
            .subscribe(new Consumer<Product>() {
                @Override
                public void accept(Product product) throws Exception {
                    TextView tvTitle = (TextView) root.findViewById(R.id.tv_update_product_title);
                    tvTitle.setText("Update " + product.getTitle());

                    EditText etTitle = (EditText) root.findViewById(R.id.et_update_product_title);
                    etTitle.setText(product.getTitle());

                    EditText etPrice = (EditText) root.findViewById(R.id.et_update_product_price);
                    etPrice.setText(String.valueOf(product.getPrice()));

                    EditText etQuantity = (EditText) root.findViewById(R.id.et_update_product_quantity);
                    String quantity = String.valueOf(product.getQuantity());
                    etQuantity.setText(quantity);

                    EditText etDescription = (EditText) root.findViewById(R.id.et_update_product_description);
                    etDescription.setText(product.getDescription());

                    // Set image
                    final ImageView ivImage = (ImageView) root.findViewById(R.id.im_product_image);
                    ivImage.getLayoutParams().width = 300;
                    ivImage.getLayoutParams().height = 300;
                    ivImage.requestLayout();
                    if (product.getImageIdentifier() != null) {
                        imageData.getImage(product.getImageIdentifier())
                                .subscribe(new Consumer<Bitmap>() {
                                    @Override
                                    public void accept(Bitmap bitmap) throws Exception {
                                        ivImage.setImageBitmap(bitmap);
                                    }
                                });
                    }

                    loadingFragment.hide();
                }
            });
    }

    // For camera
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ImageHandler imageHandler = new ImageHandler(this.getContext());
        EasyImage.handleActivityResult(requestCode, resultCode, data, this.getActivity(), imageHandler);
        imageFile = imageHandler.getImageFile();
        setProductImageView(imageFile);
    }

    public void setProductImageView(File image) {
        if (image != null) {
            String path = image.getPath();
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            ImageView ivProductImage = (ImageView) root.findViewById(R.id.im_product_image);
            ivProductImage.setVisibility(View.VISIBLE);
            ivProductImage.setImageBitmap(bitmap);

            // Hide upload image text
            TextView tvUploadCreateImage = (TextView) root.findViewById(R.id.tv_upload_create_image);
            tvUploadCreateImage.setVisibility(View.GONE);
        }
    }
}
