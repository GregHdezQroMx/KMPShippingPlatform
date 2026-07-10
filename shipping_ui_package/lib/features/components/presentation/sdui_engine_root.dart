import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../../core/bridge/ui_bridge_handler.dart';
import 'providers/form_state_provider.dart';
import 'sdui_form_renderer.dart';
import 'widgets/sdui_result_view.dart';
import '../../../shared/l10n.dart';

class SDUIEngineRoot extends ConsumerStatefulWidget {
  final String initialJson;

  const SDUIEngineRoot({super.key, required this.initialJson});

  @override
  ConsumerState<SDUIEngineRoot> createState() => _SDUIEngineRootState();
}

class _SDUIEngineRootState extends ConsumerState<SDUIEngineRoot> {
  String? _currentJson;
  Map<String, dynamic>? _successData;
  bool _isLoading = false;

  @override
  void initState() {
    super.initState();
    _currentJson = widget.initialJson;

    UIBridgeHandler.initialize(
      onRenderRequest: (json) {
        setState(() {
          _currentJson = json;
          _successData = null;
          _isLoading = false;
        });
      },
      onErrorReceived: (error) {
        // Handle validation errors by updating the form state
        final String code = error['code'] ?? '';
        final String message = error['message'] ?? '';

        // Map error code to field id if necessary, here we assume code matches or we find it
        // For simplicity, let's assume code is like "INVALID_WEIGHT" -> field "peso"
        String fieldId = _mapErrorCodeToFieldId(code);
        ref.read(sduiFormStateProvider.notifier).setError(fieldId, message);
        setState(() => _isLoading = false);
      },
      onSuccessReceived: (successJson) {
        final decoded = jsonDecode(successJson);
        setState(() {
          _successData = decoded['data'];
          _isLoading = false;
        });
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
    if (_successData != null) {
      return Scaffold(
        appBar: AppBar(title: const Text('Resultado')),
        body: Center(child: SDUIResultView(data: _successData!)),
        floatingActionButton: FloatingActionButton.extended(
          onPressed: () => UIBridgeHandler.sendEvent('RESET', {}),
          label: Text(L10n.getString('new_quote')),
          icon: const Icon(Icons.refresh),
        ),
      );
    }

    if (_currentJson == null) {
      return const Scaffold(body: Center(child: CircularProgressIndicator()));
    }

    return Stack(
      children: [
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
