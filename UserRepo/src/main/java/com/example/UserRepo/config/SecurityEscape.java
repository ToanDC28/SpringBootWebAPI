package com.example.UserRepo.config;
import com.example.UserRepo.exceptions.BadRequestException;
import com.example.UserRepo.request.RegisterRequest;
import com.example.UserRepo.request.UserUpdateRequest;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;



public class SecurityEscape {

    public static void checkRegister(RegisterRequest request) {
        if(cleanIt(request.getEmail()).isEmpty()
                || cleanIt(request.getName()).isEmpty()
                || cleanIt(request.getPhone()).isEmpty()
                || cleanIt(request.getCreatedBy()).isEmpty()){
            throw new BadRequestException("Unsafe request");
        }
    }
    public static void checkUpdateRequest(UserUpdateRequest request) {
        if(cleanIt(request.getName()).isEmpty()
                || cleanIt(request.getPhone()).isEmpty()
                || cleanIt(request.getDob()).isEmpty()){
            throw new BadRequestException("Unsafe request");
        }
    }


    public static String cleanIt(String request) {
        if(request == null || request == ""){
            return " ";
        }
        return Jsoup.clean(
                request
                , Whitelist.basic());
    }


}
