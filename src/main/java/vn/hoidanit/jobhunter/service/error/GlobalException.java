package vn.hoidanit.jobhunter.service.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import vn.hoidanit.jobhunter.domain.RestResponse;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(value = IdInvalidException.class)
    public ResponseEntity<RestResponse<Object>> handleIdInvalidException(IdInvalidException e) {
        RestResponse<Object> res = new RestResponse<Object>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError("IdInvalidException");
        res.setMessage(e.getMessage());

        return ResponseEntity.badRequest().body(res);
    }
}
