package no.gjensidige.product.controller;

import no.gjensidige.product.model.FinancialReport;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 *  RestController for the new report endpoint in exercise 2
 *
 */


@RestController
@RequestMapping(name = "Report", value = "reports")
public class ReportController {


    /**
     * Todo Create implementation for Financial report
     * as stated in exercise 2.
     *
     * @return
     */
    @GetMapping(name = "/financial")
    public FinancialReport getFinancialReport(){

        throw new UnsupportedOperationException();
    }

}
