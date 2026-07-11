import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../../core/bridge/ui_bridge_handler.dart';
import 'providers/form_state_provider.dart';
import 'sdui_form_renderer.dart';

class SDUIEngineRoot extends ConsumerStatefulWidget {
  final String initialJson;

  const SDUIEngineRoot({super.key, required this.initialJson});

  @override
  ConsumerState<SDUIEngineRoot> createState() => _SDUIEngineRootState();
}

class _SDUIEngineRootState extends ConsumerState<SDUIEngineRoot> {
  String? _currentJson;
  bool _isLoading = false;

  @override
  void initState() {
    super.initState();
    _currentJson = widget.initialJson;
    
    UIBridgeHandler.initialize(
      onRenderRequest: (json) {
        setState(() {
          _currentJson = json;
          _isLoading = false;
        });
      },
      onErrorReceived: (error) {
        final String code = error['code'] ?? '';
        final String message = error['message'] ?? '';
        String fieldId = _mapErrorCodeToFieldId(code);
        ref.read(sduiFormStateProvider.notifier).setError(fieldId, message);
        setState(() => _isLoading = false);
      },
      onSuccessReceived: (successJson) {
        // En esta versión 100% SDUI, el éxito también es un renderRequest
        // Pero mantenemos este callback por si el Host manda datos puros.
      },
    );
  }

  String _mapErrorCodeToFieldId(String code) {
    if (code.contains('WEIGHT')) return 'peso';
    if (code.contains('DISTANCE')) return 'distancia';
    if (code.contains('ZIP')) return 'codigoPostal';
    return '';
  }

  @override
  Widget build(BuildContext context) {
    if (_currentJson == null) {
      return const Scaffold(body: Center(child: CircularProgressIndicator()));
    }

    return Stack(
      children: [
        // Ahora SDUIFormRenderer es el único que maneja el Scaffold
        SDUIFormRenderer(json: _currentJson!),
        if (_isLoading)
          Container(
            color: Colors.black26,
            child: const Center(child: CircularProgressIndicator()),
          ),
      ],
    );
  }
}
