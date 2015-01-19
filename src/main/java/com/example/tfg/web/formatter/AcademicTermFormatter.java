package com.example.tfg.web.formatter;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import com.example.tfg.domain.AcademicTerm;
import com.example.tfg.repository.AcademicTermDao;

@Component
public class AcademicTermFormatter implements Formatter<AcademicTerm> {


    @Autowired
    private AcademicTermDao academicTermDao;
    //Some service class which can give the Actor after
    //fetching from Database
    
    public String print(AcademicTerm academicTerm, Locale arg1) {
          return academicTerm.getTerm();	     
    }
    
   public AcademicTerm parse(String academicTermId, Locale arg1) throws ParseException {

          return (AcademicTerm) academicTermDao.getAcademicTermById(Long.parseLong(academicTermId));
          //Else you can just return a new object by setting some values
          //which you deem fit.
     }
}
