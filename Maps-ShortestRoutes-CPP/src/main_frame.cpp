///////////////////////////////////////////////////////////////////////////
// C++ code generated with wxFormBuilder (version 3.10.1-0-g8feb16b)
// http://www.wxformbuilder.org/
//
// PLEASE DO *NOT* EDIT THIS FILE!
///////////////////////////////////////////////////////////////////////////

#include "main_frame.h"

///////////////////////////////////////////////////////////////////////////

main_frame::main_frame( wxWindow* parent, wxWindowID id, const wxString& title, const wxPoint& pos, const wxSize& size, long style ) : wxFrame( parent, id, title, pos, size, style )
{
	this->SetSizeHints( wxDefaultSize, wxDefaultSize );

	wxBoxSizer* mainBoxSizer;
	mainBoxSizer = new wxBoxSizer( wxVERTICAL );

	wxBoxSizer* file_sizer;
	file_sizer = new wxBoxSizer( wxHORIZONTAL );


	file_sizer->Add( 0, 0, 1, wxEXPAND, 5 );

	file_label = new wxStaticText( this, wxID_ANY, wxT("Select OSM file:"), wxDefaultPosition, wxDefaultSize, 0 );
	file_label->Wrap( -1 );
	file_sizer->Add( file_label, 0, wxALIGN_CENTER_VERTICAL|wxALL, 5 );

	file_picker = new wxFilePickerCtrl( this, wxID_ANY, wxEmptyString, wxT("Select a file"), wxT("*.osm"), wxDefaultPosition, wxDefaultSize, wxFLP_DEFAULT_STYLE );
	file_picker->SetMinSize( wxSize( 200,-1 ) );

	file_sizer->Add( file_picker, 0, wxALIGN_CENTER_VERTICAL|wxALL, 5 );

	parse_button = new wxButton( this, wxID_ANY, wxT("Parse"), wxDefaultPosition, wxDefaultSize, 0 );
	parse_button->Enable( false );

	file_sizer->Add( parse_button, 0, wxALIGN_CENTER_VERTICAL|wxALL, 5 );


	file_sizer->Add( 0, 0, 1, wxEXPAND, 5 );


	mainBoxSizer->Add( file_sizer, 1, wxEXPAND, 5 );

	wxBoxSizer* nodes_ways;
	nodes_ways = new wxBoxSizer( wxHORIZONTAL );


	nodes_ways->Add( 0, 0, 1, wxEXPAND, 5 );

	nodes_label = new wxStaticText( this, wxID_ANY, wxT("Nodes:"), wxDefaultPosition, wxDefaultSize, 0 );
	nodes_label->Wrap( -1 );
	nodes_ways->Add( nodes_label, 0, wxALIGN_CENTER_VERTICAL|wxALL, 5 );

	nodes_output = new wxTextCtrl( this, wxID_ANY, wxEmptyString, wxDefaultPosition, wxDefaultSize, 0 );
	nodes_output->Enable( false );

	nodes_ways->Add( nodes_output, 0, wxALIGN_CENTER_VERTICAL|wxALL, 5 );

	ways_label = new wxStaticText( this, wxID_ANY, wxT("Ways:"), wxDefaultPosition, wxDefaultSize, 0 );
	ways_label->Wrap( -1 );
	nodes_ways->Add( ways_label, 0, wxALIGN_CENTER_VERTICAL|wxALL, 5 );

	ways_output = new wxTextCtrl( this, wxID_ANY, wxEmptyString, wxDefaultPosition, wxDefaultSize, 0 );
	ways_output->Enable( false );

	nodes_ways->Add( ways_output, 0, wxALIGN_CENTER_VERTICAL|wxALL, 5 );


	nodes_ways->Add( 0, 0, 1, wxEXPAND, 5 );


	mainBoxSizer->Add( nodes_ways, 1, wxEXPAND, 5 );

	wxBoxSizer* search_query_sizer;
	search_query_sizer = new wxBoxSizer( wxHORIZONTAL );


	search_query_sizer->Add( 0, 0, 1, wxEXPAND, 5 );

	query_label = new wxStaticText( this, wxID_ANY, wxT("Search:"), wxDefaultPosition, wxDefaultSize, 0 );
	query_label->Wrap( -1 );
	search_query_sizer->Add( query_label, 0, wxALIGN_CENTER_VERTICAL|wxALL, 5 );

	query_input = new wxTextCtrl( this, wxID_ANY, wxEmptyString, wxDefaultPosition, wxDefaultSize, 0 );
	search_query_sizer->Add( query_input, 0, wxALIGN_CENTER_VERTICAL|wxALL, 5 );

	go_button = new wxButton( this, wxID_ANY, wxT("Go"), wxDefaultPosition, wxDefaultSize, 0 );
	go_button->Enable( false );

	search_query_sizer->Add( go_button, 0, wxALIGN_CENTER_VERTICAL|wxALL, 5 );

	query_results = new wxListBox( this, wxID_ANY, wxDefaultPosition, wxDefaultSize, 0, NULL, 0 );
	query_results->SetMinSize( wxSize( 200,150 ) );

	search_query_sizer->Add( query_results, 0, wxALIGN_CENTER_VERTICAL|wxALL, 5 );


	search_query_sizer->Add( 0, 0, 1, wxEXPAND, 5 );


	mainBoxSizer->Add( search_query_sizer, 1, wxEXPAND, 5 );

	wxBoxSizer* k_closest_sizer;
	k_closest_sizer = new wxBoxSizer( wxHORIZONTAL );


	k_closest_sizer->Add( 0, 0, 1, wxEXPAND, 5 );

	target_node_label = new wxStaticText( this, wxID_ANY, wxT("Target Node ID:"), wxDefaultPosition, wxDefaultSize, 0 );
	target_node_label->Wrap( -1 );
	k_closest_sizer->Add( target_node_label, 0, wxALIGN_CENTER_VERTICAL|wxALL, 5 );

	target_id_input = new wxTextCtrl( this, wxID_ANY, wxEmptyString, wxDefaultPosition, wxDefaultSize, 0 );
	k_closest_sizer->Add( target_id_input, 0, wxALIGN_CENTER_VERTICAL|wxALL, 5 );

	k_label = new wxStaticText( this, wxID_ANY, wxT("k:"), wxDefaultPosition, wxDefaultSize, 0 );
	k_label->Wrap( -1 );
	k_closest_sizer->Add( k_label, 0, wxALIGN_CENTER_VERTICAL|wxALL, 5 );

	k_input = new wxTextCtrl( this, wxID_ANY, wxEmptyString, wxDefaultPosition, wxDefaultSize, 0 );
	k_closest_sizer->Add( k_input, 0, wxALIGN_CENTER_VERTICAL|wxALL, 5 );

	compute_button = new wxButton( this, wxID_ANY, wxT("Compute"), wxDefaultPosition, wxDefaultSize, 0 );
	compute_button->Enable( false );

	k_closest_sizer->Add( compute_button, 0, wxALIGN_CENTER_VERTICAL|wxALL, 5 );


	k_closest_sizer->Add( 0, 0, 1, wxEXPAND, 5 );


	mainBoxSizer->Add( k_closest_sizer, 1, wxEXPAND, 5 );

	wxBoxSizer* k_results_sizer;
	k_results_sizer = new wxBoxSizer( wxVERTICAL );

	table = new wxListCtrl( this, wxID_ANY, wxDefaultPosition, wxDefaultSize, wxLC_REPORT );
	k_results_sizer->Add( table, 0, wxALL|wxEXPAND, 5 );


	mainBoxSizer->Add( k_results_sizer, 1, wxEXPAND, 5 );

	wxBoxSizer* dijkstra_sizer;
	dijkstra_sizer = new wxBoxSizer( wxHORIZONTAL );


	dijkstra_sizer->Add( 0, 0, 1, wxEXPAND, 5 );

	start_label = new wxStaticText( this, wxID_ANY, wxT("Start node ID:"), wxDefaultPosition, wxDefaultSize, 0 );
	start_label->Wrap( -1 );
	dijkstra_sizer->Add( start_label, 0, wxALIGN_CENTER_VERTICAL|wxALL, 5 );

	start_input = new wxTextCtrl( this, wxID_ANY, wxEmptyString, wxDefaultPosition, wxDefaultSize, 0 );
	dijkstra_sizer->Add( start_input, 0, wxALIGN_CENTER_VERTICAL|wxALL, 5 );

	end_label = new wxStaticText( this, wxID_ANY, wxT("End node ID:"), wxDefaultPosition, wxDefaultSize, 0 );
	end_label->Wrap( -1 );
	dijkstra_sizer->Add( end_label, 0, wxALIGN_CENTER_VERTICAL|wxALL, 5 );

	end_input = new wxTextCtrl( this, wxID_ANY, wxEmptyString, wxDefaultPosition, wxDefaultSize, 0 );
	dijkstra_sizer->Add( end_input, 0, wxALIGN_CENTER_VERTICAL|wxALL, 5 );

	calculate_button = new wxButton( this, wxID_ANY, wxT("Calculate"), wxDefaultPosition, wxDefaultSize, 0 );
	calculate_button->Enable( false );

	dijkstra_sizer->Add( calculate_button, 0, wxALIGN_CENTER_VERTICAL|wxALL, 5 );


	dijkstra_sizer->Add( 0, 0, 1, wxEXPAND, 5 );


	mainBoxSizer->Add( dijkstra_sizer, 1, wxEXPAND, 5 );

	wxBoxSizer* dijkstra_result;
	dijkstra_result = new wxBoxSizer( wxHORIZONTAL );


	dijkstra_result->Add( 0, 0, 1, wxEXPAND, 5 );

	answer_label = new wxStaticText( this, wxID_ANY, wxT("Shortest distance:"), wxDefaultPosition, wxDefaultSize, 0 );
	answer_label->Wrap( -1 );
	dijkstra_result->Add( answer_label, 0, wxALIGN_CENTER_VERTICAL|wxALL, 5 );

	answer_output = new wxTextCtrl( this, wxID_ANY, wxEmptyString, wxDefaultPosition, wxDefaultSize, 0 );
	answer_output->Enable( false );

	dijkstra_result->Add( answer_output, 0, wxALIGN_CENTER_VERTICAL|wxALL, 5 );


	dijkstra_result->Add( 0, 0, 1, wxEXPAND, 5 );


	mainBoxSizer->Add( dijkstra_result, 1, wxEXPAND, 5 );

	mapSizer = new wxBoxSizer( wxVERTICAL );


	mainBoxSizer->Add( mapSizer, 1, wxEXPAND, 5 );


	this->SetSizer( mainBoxSizer );
	this->Layout();

	this->Centre( wxBOTH );

	// Connect Events
	file_picker->Connect( wxEVT_COMMAND_FILEPICKER_CHANGED, wxFileDirPickerEventHandler( main_frame::on_file_select ), NULL, this );
	parse_button->Connect( wxEVT_COMMAND_BUTTON_CLICKED, wxCommandEventHandler( main_frame::on_parse_click ), NULL, this );
	go_button->Connect( wxEVT_COMMAND_BUTTON_CLICKED, wxCommandEventHandler( main_frame::on_go_click ), NULL, this );
	compute_button->Connect( wxEVT_COMMAND_BUTTON_CLICKED, wxCommandEventHandler( main_frame::on_compute_click ), NULL, this );
	calculate_button->Connect( wxEVT_COMMAND_BUTTON_CLICKED, wxCommandEventHandler( main_frame::on_calculate_click ), NULL, this );
}

main_frame::~main_frame()
{
	// Disconnect Events
	file_picker->Disconnect( wxEVT_COMMAND_FILEPICKER_CHANGED, wxFileDirPickerEventHandler( main_frame::on_file_select ), NULL, this );
	parse_button->Disconnect( wxEVT_COMMAND_BUTTON_CLICKED, wxCommandEventHandler( main_frame::on_parse_click ), NULL, this );
	go_button->Disconnect( wxEVT_COMMAND_BUTTON_CLICKED, wxCommandEventHandler( main_frame::on_go_click ), NULL, this );
	compute_button->Disconnect( wxEVT_COMMAND_BUTTON_CLICKED, wxCommandEventHandler( main_frame::on_compute_click ), NULL, this );
	calculate_button->Disconnect( wxEVT_COMMAND_BUTTON_CLICKED, wxCommandEventHandler( main_frame::on_calculate_click ), NULL, this );

}
