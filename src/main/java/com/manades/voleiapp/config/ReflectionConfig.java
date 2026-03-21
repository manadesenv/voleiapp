package com.manades.voleiapp.config;

import com.manades.voleiapp.helper.PartitsHelper;
import com.manades.voleiapp.model.federatio.*;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.context.annotation.Configuration;

@Configuration
@RegisterReflectionForBinding({
		PartitsHelper.PartitsResponse.class,
		Categoria.class,
		Competicion.class,
		Fase.class,
		Grupo.class,
		Partido.class,
})
public class ReflectionConfig {
}
