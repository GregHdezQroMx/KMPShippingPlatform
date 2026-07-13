import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'providers/sdui_state_provider.dart';
import 'sdui_form_renderer.dart';

class SDUIEngineRoot extends ConsumerStatefulWidget {
  final String initialJson;
  final Function(String event, Map<String, dynamic> data)? onEvent;

  const SDUIEngineRoot({
    super.key, 
    required this.initialJson,
    this.onEvent,
  });

  @override
  ConsumerState<SDUIEngineRoot> createState() => _SDUIEngineRootState();
}

class _SDUIEngineRootState extends ConsumerState<SDUIEngineRoot> {
  @override
  void initState() {
    super.initState();
    // Sincronizamos el estado de Riverpod en segundo plano, 
    // pero el build NO dependerá de esto para el primer render.
    WidgetsBinding.instance.addPostFrameCallback((_) {
      if (mounted) {
        ref.read(sduiStateProvider.notifier).updateJson(widget.initialJson);
        if (widget.onEvent != null) {
          ref.read(sduiStateProvider.notifier).setEventHandler(widget.onEvent!);
        }
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    // Escuchamos el estado global para actualizaciones reactivas
    final stateJson = ref.watch(sduiStateProvider.select((s) => s.json));
    
    // Si el provider tiene un nuevo JSON (distinto al inicial), lo usamos.
    // De lo contrario, mantenemos el del constructor.
    final jsonToRender = (stateJson.isNotEmpty && stateJson != widget.initialJson) 
        ? stateJson 
        : widget.initialJson;

    return SDUIFormRenderer(json: jsonToRender);
  }
}
