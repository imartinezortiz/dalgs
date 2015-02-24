Proyecto TFG
 -- TO DO --
 View an academicTerm -->  
 		Filter target must be a collection or array type 	
 		@PostAuthorize("hasPermission(returnObject, 'WRITE')")
 
 Delete an academicTerm -->
		Unauthorized
		@PreAuthorize("hasPermission(#post, 'WRITE')")
	
/ Almudena
