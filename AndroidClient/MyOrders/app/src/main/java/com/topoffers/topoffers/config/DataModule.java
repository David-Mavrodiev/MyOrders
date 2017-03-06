package com.topoffers.topoffers.config;

import com.topoffers.data.base.IData;
import com.topoffers.data.base.IImageData;
import com.topoffers.data.services.HttpRestData;
import com.topoffers.data.services.ImagesHttpData;
import com.topoffers.topoffers.common.models.ApiUrl;
import com.topoffers.topoffers.common.models.LoginRequest;
import com.topoffers.topoffers.common.models.LoginResult;
import com.topoffers.topoffers.common.models.Order;
import com.topoffers.topoffers.common.models.Product;
import com.topoffers.topoffers.common.models.ProductsCart;
import com.topoffers.topoffers.common.models.Profile;
import com.topoffers.topoffers.common.models.RegisterRequest;
import com.topoffers.topoffers.common.models.RegisterResult;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class DataModule {
    private ProductsCart cart = new ProductsCart();
    @Provides
    ApiUrl<LoginRequest> provideBookApiUrl(@Named("apiBaseUrl") String apiBaseUrl) {
        ApiUrl<LoginRequest> apiUrlLogin = new ApiUrl<>(apiBaseUrl, "login");
        return apiUrlLogin;
    }

    //Added register
    @Provides
    ApiUrl<RegisterRequest> provideRegisterApiUrl(@Named("apiBaseUrl") String apiBaseUrl){
        ApiUrl<RegisterRequest> apiUrlRegister = new ApiUrl<>(apiBaseUrl, "register");
        return  apiUrlRegister;
    }

    @Provides
    ApiUrl<Product> provideProductApiUrl(@Named("apiBaseUrl") String apiBaseUrl) {
        ApiUrl<Product> apiUrlProduct = new ApiUrl<>(apiBaseUrl, "products");
        return apiUrlProduct;
    }

    //Added register
    @Provides
    @Inject
    IData<RegisterResult> provideIDataRegisterResult(ApiUrl<RegisterRequest> apiUrlRegister) {
        return new HttpRestData<RegisterResult>(apiUrlRegister.getUrl(), RegisterResult.class, RegisterResult[].class);
    }

    @Provides
    ApiUrl<Order> provideOrderApiUrl(@Named("apiBaseUrl") String apiBaseUrl) {
        ApiUrl<Order> apiUrlOrder = new ApiUrl<>(apiBaseUrl, "orders");
        return apiUrlOrder;
    }

    @Provides
    @Inject
    IData<LoginResult> provideIDataLoginResult(ApiUrl<LoginRequest> apiUrlrlLogin) {
        return  new HttpRestData<LoginResult>(apiUrlrlLogin.getUrl(), LoginResult.class, LoginResult[].class);
    }

    @Provides
    @Inject
    IData<Product> provideIDataProduct(ApiUrl<Product> apiUrlProduct) {
        return new HttpRestData<Product>(apiUrlProduct.getUrl(), Product.class, Product[].class);
    }

    @Provides
    @Inject
    IData<Order> provideOrder(ApiUrl<Order> apiUrlOrder) {
        return new HttpRestData<Order>(apiUrlOrder.getUrl(), Order.class, Order[].class);
    }

    @Provides
    ProductsCart provideProductsCart(){
        return cart;
    }

    @Provides
    IImageData provideImageData(@Named("apiBaseImageUrl") String apiImageUrl) {
        return new ImagesHttpData(apiImageUrl);
    }

    @Provides
    ApiUrl<Profile> provideProfileApiUrl(@Named("apiBaseUrl") String apiBaseUrl) {
        ApiUrl<Profile> apiUrlProfile = new ApiUrl<>(apiBaseUrl, "profile");
        return apiUrlProfile;
    }

    @Provides
    @Inject
    IData<Profile> provideProfile(ApiUrl<Profile> apiUrlProfile) {
        return new HttpRestData<Profile>(apiUrlProfile.getUrl(), Profile.class, Profile[].class);
    }
}
