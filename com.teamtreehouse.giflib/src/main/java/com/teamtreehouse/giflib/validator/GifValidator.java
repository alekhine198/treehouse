package com.teamtreehouse.giflib.validator;


import com.teamtreehouse.giflib.model.Gif;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class GifValidator implements Validator {

    @Autowired
    private Validator validator;
    @Override
    public boolean supports(Class<?> clazz) {
        return Gif.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Gif gif = (Gif) target;
        // Validate file only if the Gif's id is null (with a null id, it must be a new Gif),
        // so that existing Gif can be updated without uploading new file
        if(gif.getId() == null && (gif.getFile() == null || gif.getFile().isEmpty())) {
            errors.rejectValue("file","file.required","Please choose a file to upload");
        }

        // Validate description
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"description","description.empty","Please enter a description");

        // Validate category
        ValidationUtils.rejectIfEmpty(errors,"category","category.empty","Please choose a category");
    }
}
