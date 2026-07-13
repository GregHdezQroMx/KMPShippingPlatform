import 'package:flutter_riverpod/flutter_riverpod.dart';

/// El estado global del motor SDUI (Agnóstico)
class SDUIState {
  final String json;
  final Function(String event, Map<String, dynamic> data)? onEvent;

  SDUIState({this.json = '', this.onEvent});

  SDUIState copyWith({
    String? json,
    Function(String event, Map<String, dynamic> data)? onEvent,
  }) {
    return SDUIState(
      json: json ?? this.json,
      onEvent: onEvent ?? this.onEvent,
    );
  }
}

class SDUIStateNotifier extends StateNotifier<SDUIState> {
  SDUIStateNotifier() : super(SDUIState());

  void updateJson(String json) => state = state.copyWith(json: json);
  void setEventHandler(Function(String, Map<String, dynamic>) handler) => 
      state = state.copyWith(onEvent: handler);
}

final sduiStateProvider = StateNotifierProvider<SDUIStateNotifier, SDUIState>((ref) {
  return SDUIStateNotifier();
});
