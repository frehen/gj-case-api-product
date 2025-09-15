package no.gjensidige.product.service;

import no.gjensidige.product.model.FinancialReport;
import org.springframework.stereotype.Service;

@Service
public class ReportService {
    public FinancialReport getFinancialReport() {
        return new FinancialReport();
    }
}
