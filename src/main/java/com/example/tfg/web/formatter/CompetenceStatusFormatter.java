package com.example.tfg.web.formatter;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import com.example.tfg.domain.CompetenceStatus;
import com.example.tfg.repository.CompetenceDao;

@Component
public class CompetenceStatusFormatter implements Formatter<CompetenceStatus> {


    @Autowired
    private CompetenceDao competenceDao;
    //Some service class which can give the Actor after
    //fetching from Database
    
    public String print(CompetenceStatus competencestatus, Locale arg1) {
          return competencestatus.getCompetence().getName();	     
    }
    
   public CompetenceStatus parse(String competenceId, Locale arg1) throws ParseException {
	   	CompetenceStatus competencestatus = new CompetenceStatus();
        competencestatus.setCompetence(competenceDao.getCompetence(Long.parseLong(competenceId)));
        return competencestatus;
          //Else you can just return a new object by setting some values
          //which you deem fit.
     }
}
