package com.upc.pre.peaceapp.reports.interfaces.rest.transform;

import com.upc.pre.peaceapp.reports.domain.model.commands.CreateReportCommand;
import com.upc.pre.peaceapp.reports.interfaces.rest.resources.CreateReportResource;
import org.springframework.stereotype.Component;

@Component
public class CreateReportCommandFromResourceAssembler {

    public CreateReportCommand toCommand(CreateReportResource resource) {
        return new CreateReportCommand(
                resource.title(),
                resource.description(),
                resource.location(),
                resource.type(),
                resource.userId(),
                resource.imageUrl(),
                resource.latitude(),
                resource.longitude()
        );
    }
}
