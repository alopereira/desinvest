package com.totvs.tj.tcc.infra.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.totvs.tj.tcc.app.empresa.EmpresaApplicationService;
import com.totvs.tj.tcc.app.empresa.SalvaEmpresaCommand;
import com.totvs.tj.tcc.domain.empresa.EmpresaId;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/empresa")
@Api(value = "/empresa", consumes = "application/json")
public class EmpresaController {
    
    @RequestMapping(value = "/", method = RequestMethod.POST, produces = "application/json")
    @ApiOperation(value = "Cria empresa",
            notes = "Cria uma empresa nova no sistema",
            response = EmpresaId.class)
    public EmpresaId activate(@RequestBody SalvaEmpresaCommand cmd) {
        EmpresaId empresaId = EmpresaApplicationService.builder()
                .build().handle(cmd);

        return empresaId;
    }
    
}
