package com.example.tfg.web.formatter;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import com.example.tfg.domain.Competence;
import com.example.tfg.repository.CompetenceDao;

@Component
public class CompetenceFormatter implements Formatter<Competence> {


    @Autowired
    private CompetenceDao competenceDao;
    //Some service class which can give the Actor after
    //fetching from Database
    
    public String print(Competence competence, Locale arg1) {
          return competence.getName();	     
    }
    
   public Competence parse(String competenceId, Locale arg1) throws ParseException {

          return competenceDao.getCompetence(Long.parseLong(competenceId));
          //Else you can just return a new object by setting some values
          //which you deem fit.
     }
}
